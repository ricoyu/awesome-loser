package com.loserico.java15;

public class TestSealedInterface {

}

sealed interface I permits A {
}

non-sealed class A implements I {
}

sealed interface MyInter1 permits MyInter3{}
interface MyInter2{}
non-sealed interface MyInter3 extends MyInter1, MyInter2{}


