package com.loserico.pattern.visitor3;

public class Computer implements ComputerPart {
	
	private ComputerPart[] computerParts;
	
	public Computer() {
		computerParts = new ComputerPart[] {new Mouse(), new Keyboard(), new Monitor()};
	}

	@Override
	public void accept(ComputerPartVisitor computerPartVisitor) {
		for (int i = 0; i < computerParts.length; i++) {
			ComputerPart computerPart = computerParts[i];
			computerPart.accept(computerPartVisitor);
		}
		computerPartVisitor.visit(this);
	}

}
