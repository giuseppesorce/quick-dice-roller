package ohm.dexp.function;

public class TokenFunctionDWars extends TokenFunctionBWheel {

	@Override
	protected int initChildNumber() {
		return 4;
	}

	@Override
	public int getType() {
		return 76;
	}

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	protected boolean extraSuccessOnRollAgain() {
		return true;
	}
}
