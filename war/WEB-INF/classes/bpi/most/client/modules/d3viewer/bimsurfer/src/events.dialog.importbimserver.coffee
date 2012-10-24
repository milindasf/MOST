# Eventful code comes here
# Program state should not be manipulated outside events files

# Clear info/error/warning (etc) messages in the dialog
bimserverImportDialogClearMessages = () ->
  ($ '#bimserver-import-message-info').html ''
  ($ '#bimserver-import-message-error').html ''
  ($ '.error').removeClass 'error'

# Display the dialog
bimserverImportDialogShow = () ->
  bimserverImportDialogShowTab1()
  ($ '#dialog-background,#dialog-bimserver-import').show()

# Display the first tab
bimserverImportDialogShowTab1 = () ->
  bimserverImportDialogClearMessages()
  $stepElements = $ '#dialog-bimserver-import .dialog-step'
  ($ $stepElements.get(0)).addClass 'dialog-step-active'
  ($ $stepElements.get(1)).removeClass 'dialog-step-active'
  ($ '#dialog-tab-bimserver1').show()
  ($ '#dialog-tab-bimserver2').hide()

# Display the second tab
bimserverImportDialogShowTab2 = () ->
  bimserverImportDialogClearMessages()
  $stepElements = $ '#dialog-bimserver-import .dialog-step'
  ($ $stepElements.get(0)).removeClass 'dialog-step-active'
  ($ $stepElements.get(1)).addClass 'dialog-step-active'
  ($ '#dialog-tab-bimserver1').hide()
  ($ '#dialog-tab-bimserver2').show()

# Open the second tab (if the bimserver connection is active)
bimserverImportDialogToggleTab2 = () ->
  # TODO: Test whether server bimserver connection is active
  bimserverImportDialogShowTab2()

# Send a login request to the bimserver
bimserverImportDialogLogin = () ->
  # Clear the message fields
  bimserverImportDialogClearMessages()
  ($ 'bimserver-projects').html ""

  url = ($ '#bimserver-login-url').val()
  user = ($ '#bimserver-login-username').val()
  pwd = ($ '#bimserver-login-password').val()

  # Validate inputs
  valid = true
  if url.length < 1
    ($ '#bimserver-login-url').addClass 'error'
    valid = false
  if user.length < 1
    ($ '#bimserver-login-username').addClass 'error'
    valid = false
  if pwd.length < 1
    ($ '#bimserver-login-password').addClass 'error'
    valid = false
  
  if not valid
    ($ '#bimserver-import-message-error').html "Some fields are incorrect"
    return false

  # Disable the login button and all form elements once the login button has been hit
  ($ '#dialog-tab-bimserver1 input, #dialog-tab-bimserver1 button').attr 'disabled', 'disabled'

  # NOTE: Writing a correct url validation regex (with no false positives or false negatives) is close to impossible.
  # Instead we are relying on the browsers validation on the field itself and then simply try to use the url.
  # If the server answers, the url is clearly correct.

  # Ensure root url ends with /
  if url[url.length - 1] != '/'
    url += '/'
  
  # TODO: Ping the url to make sure it's correct? (Is it necessary?)

  # Call the REST api
  # TODO: Would be nice to find a more secure way to login (without using clear text)
  ($ '#bimserver-import-message-info').html "Sending login request..."
  ($.ajax
    username: encodeURIComponent user
    password: encodeURIComponent pwd
    #url: url + 'rest/login'
    url: url + 'login.jsp'
    data: 'username=' + (encodeURIComponent user) + '&password=' + (encodeURIComponent pwd))
    .done (data, textStatus, jqXHR) -> 
      ($ '#bimserver-import-message-info').html "Login request succeeded"
      bimserverImportDialogShowTab2()
      bimserverImportDialogRefresh()
    .fail (jqXHR, textStatus, errorThrown) -> 
      ($ '#bimserver-import-message-info').html ""
      ($ '#bimserver-import-message-error').html "Login request failed"
    .always (jqXHR, textStatus, errorThrown) -> 
      ($ '#dialog-tab-bimserver1 input, #dialog-tab-bimserver1 button').removeAttr 'disabled'

  pwd = null
  return true

# Send a request to the bimserver to refresh the list of available projects
bimserverImportDialogRefresh = () ->
  url = ($ '#bimserver-login-url').val()

  # Ensure root url ends with /
  if url[url.length - 1] != '/'
    url += '/'
  
  ($ '#dialog-tab-bimserver2 button').attr 'disabled', 'disabled'
  ($ '#bimserver-projects-submit').attr 'disabled', 'disabled'
  
  # Refresh list of projects using the bimserver rest interface
  $projectList = $ '#bimserver-projects'
  $projectList.html ""

  ($.get url + 'rest/getAllProjects', undefined, undefined, 'xml')
    .done (data, textStatus, jqXHR) -> 
      #console.log data
      ($ '#bimserver-import-message-info').html "Fetched all projects"
      (($ data).find 'sProject').each () ->
        $projectList.append "<li class='bimserver-project' bimserveroid='" + (($ this).find 'oid').text() + "' bimserverroid='" + (($ this).find 'revisions').text() + "'>" + (($ this).find 'name').text() + "</li>"
    .fail (jqXHR, textStatus, errorThrown) -> 
      ($ '#bimserver-import-message-info').html ''
      ($ '#bimserver-import-message-error').html "Couldn't fetch projects"
    .always (jqXHR, textStatus, errorThrown) -> 
      ($ '#dialog-tab-bimserver2 button').removeAttr 'disabled'

# Select a project in the list and enable the load button
bimserverImportDialogSelect = (event) ->
  ($ '.bimserver-project-selected').removeClass 'bimserver-project-selected'
  ($ event.target).addClass 'bimserver-project-selected'
  ($ '#bimserver-projects-submit').removeAttr 'disabled'

# Load the selected project from the bimserver
bimserverImportDialogLoad = () ->
  $selectedProject = $ '.bimserver-project-selected'
  if $selectedProject.length == 0
    ($ '#bimserver-import-message-error').html "No project selected"
  else
    # Close the dialog
    hideDialog()

    # Ensure root url ends with /
    url = ($ '#bimserver-login-url').val()
    if url[url.length - 1] != '/'
      url += '/'
    
    # Load the model
    bimserverImport url, $selectedProject.attr 'bimserverroid'

