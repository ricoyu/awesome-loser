package com.loserico.pattern.iterator.iterators;


import com.loserico.pattern.iterator.profile.Profile;

public interface ProfileIterator {
    boolean hasNext();

    Profile getNext();

    void reset();
}
