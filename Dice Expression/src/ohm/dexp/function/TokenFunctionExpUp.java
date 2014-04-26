package ohm.dexp.function;

import ohm.dexp.DInstance;
import ohm.dexp.exception.DException;

public class TokenFunctionExpUp extends TokenFunctionExplodeBase {

	@Override
	protected int initChildNumber() {
		return 1;
	}

	@Override
	public int getType() {
		return 21;
	}

	@Override
	protected void evaluateSelf(DInstance instance) throws DException {
		evaluateExplode(instance, getChild(1), 1, false);
	}

}
