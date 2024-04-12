package com.loserico.pattern.behavioral.memento1;

public class MementoPatternDemo {

    public static void main(String[] args) {

        Originator originator = new Originator();
        CareTaker careTaker = new CareTaker();
        originator.setState("State #1");
        System.out.println("Current State: " + originator.getState());
        originator.setState("State #2");
        System.out.println("Current State: " + originator.getState());
        careTaker.add(originator.saveStateToMemento());
        originator.setState("State #3");
        System.out.println("Current State: " + originator.getState());
        careTaker.add(originator.saveStateToMemento());
        originator.setState("State #4");
        System.out.println("Current State: " + originator.getState());

        System.out.println("Current State: " + originator.getState());
        originator.getStateFromMemento(careTaker.get(0));
        System.out.println("First saved State: " + originator.getState());
        originator.getStateFromMemento(careTaker.get(1));
        System.out.println("Second saved State: " + originator.getState());
    }
}