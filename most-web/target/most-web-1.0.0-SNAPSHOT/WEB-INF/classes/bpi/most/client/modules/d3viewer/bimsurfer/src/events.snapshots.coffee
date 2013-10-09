# Eventful code comes here
# Program state should not be manipulated outside events files

snapshotsPush = () ->
  if not state.scene?  
    return
  
  # Force rendering a frame in webkit
  # See issue #19 (https://github.com/bimserver/BIMsurfer/issues/19)
  # And also discussion at https://groups.google.com/forum/?pli=1#!topic/scenejs/YjZy3Dd5mbA
  if $.browser.webkit
    state.scene.renderFrame { force: true }

  thumbSize = constants.thumbnails.size  
  imgURI = canvasCaptureThumbnail state.canvas, 512 * thumbSize[0] / thumbSize[1], 512, constants.thumbnails.scale * thumbSize[0], constants.thumbnails.scale * thumbSize[1]
  node = state.scene.findNode 'main-lookAt'
  state.snapshots.lookAts.push
    eye: node.get 'eye'
    look: node.get 'look'
    up: node.get 'up'
  ($ '#snapshots').append "
<div class='snapshot'>
<div class='snapshot-thumb'>
<a href='#' class='snapshot-delete'>x</a>
<img width='" + thumbSize[0] + "px' height='" + thumbSize[1] + "px' src='" + imgURI + "'>
</div>
</div>"

snapshotsDelete = (event) ->
  $parent = ($ event.target).parent()
  state.snapshots.lookAts.splice $parent.index() + 1, 1
  $parent.remove()

# Show a toggled snapshot in the main view
snapshotsToggle = (event) ->
  if not state.scene?  
    return
  # TODO: SceneJS.FX.transition (state.scene.findNode 'main-lookAt'), state.snapshots, { interpolation: 'linear' }

snapshotsPlay = (event) ->
  if not state.scene?  
    return
  (SceneJS.FX.TweenSpline state.scene.findNode 'main-lookAt').sequence state.snapshots.lookAts

