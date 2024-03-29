package ohm.dexp.function;

import ohm.dexp.DContext;
import ohm.dexp.TokenBase;
import ohm.dexp.exception.DException;

public class TokenFunctionRoundDown extends TokenFunction {

	@Override
	protected int initChildNumber() {
		return 1;
	}

	@Override
	public int getType() {
		return 31;
	}

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	protected void evaluateSelf(DContext instance) throws DException {
		TokenBase param;
		
		param = getChild(1);
		
		param.evaluate(instance);
		
		resultValue = round(param.getRawResult());
		
		resultMaxValue = round(param.getMaxResult());
		resultMinValue = round(param.getMinResult());

		//resultString = "{" + param.getResultString() + "}";
		resultString = CH_RDN_OP + param.getResultString() + CH_RDN_CL;
	}

	protected long round(long value) {
		long decimal = value % VALUES_PRECISION_FACTOR;
		if (decimal < 0) {
			return value - VALUES_PRECISION_FACTOR - decimal;
		} else /* if (decimal > 0) */ {
			return value - decimal;
		//} else {
		//	return value;
		}
	}
	
}
