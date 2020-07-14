package com.loserico.pattern.singleton;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

public class EagerAntiSerializeCloneClassloaderSingletonTest {

	public static void main(String[] args) {
		cloneInstance();
	}

	private static void cloneInstance() {
		EagerAntiSerializeCloneClassloaderSingleton instance1 = EagerAntiSerializeCloneClassloaderSingleton.getInstance();
		EagerAntiSerializeCloneClassloaderSingleton instance2 = null;
		try {
			instance2 = (EagerAntiSerializeCloneClassloaderSingleton) instance1.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		System.out.println("instance1 hashCode=" + instance1.hashCode());
        System.out.println("instance2 hashCode=" + instance2.hashCode());
	}

	private static void serializeObject() {
		try {
        	EagerAntiSerializeCloneClassloaderSingleton instance1 = EagerAntiSerializeCloneClassloaderSingleton.getInstance();
            ObjectOutput out = null;
            out = new ObjectOutputStream(new FileOutputStream("filename.ser"));
            out.writeObject(instance1);
            out.close();
            //deserialize from file to object
            ObjectInput in = new ObjectInputStream(new FileInputStream("filename.ser"));
            EagerAntiSerializeCloneClassloaderSingleton instance2 = (EagerAntiSerializeCloneClassloaderSingleton) in.readObject();
            in.close();
            System.out.println("instance1 hashCode=" + instance1.hashCode());
            System.out.println("instance2 hashCode=" + instance2.hashCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}
