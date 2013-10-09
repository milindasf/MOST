package bpi.most.client.utils;

import java.util.ArrayList;

/**
 * This class represents an observable object, or "data" in the model-view paradigm. It can be subclassed to represent an object that the application wants to have observed.
 * An observable object can have one or more observers. An observer may be any object that implements interface Observer. After an observable instance changes, an application calling the Observable's notifyObservers method causes all of its observers to be notified of the change by a call to their update method.
 * The order in which notifications will be delivered is unspecified. The default implementation provided in the Observerable class will notify Observers in the order in which they registered interest, but subclasses may change this order, use no guaranteed order, deliver notifications on separate threads, or may guarantee that their subclass follows this order, as they choose.
 * Note that this notification mechanism is has nothing to do with threads and is completely separate from the wait and notify mechanism of class Object.
 * When an observable object is newly created, its set of observers is empty. Two observers are considered the same if and only if the equals method returns true for them.
 * 
 * @author jowe
 *
 */
public class Observable {
	/**
	 * Observable object have been changed?
	 */
	private boolean changed = false;
	
	/**
	 * Contain all attached observers
	 */
	private ArrayList<Observer> observers = new ArrayList<Observer>();
	
	/**
	 * Adds an observer to the set of observers for this object, provided that it is not the same as some observer already in the set.
	 * @param observer
	 */
	public void addObserver(Observer observer){
		observers.add(observer);
	}
	
	/**
	 * If this object has changed, as indicated by the hasChanged method, then notify all of its observers and then call the clearChanged method to indicate that this object has no longer changed.
	 * @param arg
	 */
	public void notifyObservers(Object arg){
		if(this.hasChanged())
		{
			for(Observer observer : this.observers)
			{
				observer.update(this, arg);
			}
			this.clearChanged();
		}
	}

	/**
	 * Deletes an observer from the set of observers of this object.
	 * @param observer
	 */
	public void deleteObserver(Observer observer){
		if(this.observers.contains(observer))
		{
			this.observers.remove(observer);
		}
	}
	
	/**
	 * Clears the observer list so that this object no longer has any observers.
	 */
	public void deleteObservers(){
		this.observers.clear();
	}
	
	/**
	 * Tests if this object has changed.
	 * @return bool
	 */
	public boolean hasChanged(){
		return this.changed;
	}
	
	/**
	 * Marks this Observable object as having been changed; the hasChanged method will now return true.
	 */
	protected void setChanged(){
		this.changed = true;
	}
	
	/**
	 * Indicates that this object has no longer changed, or that it has already notified all of its observers of its most recent change, so that the hasChanged method will now return false.
	 */
	protected void clearChanged(){
		this.changed = false;
	}
	
	/**
	 * Returns the number of observers of this Observable object.
	 * @return int
	 */
	public int countObservers(){
		return this.observers.size();
	}
}
