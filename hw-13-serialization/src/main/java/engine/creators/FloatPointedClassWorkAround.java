package engine.creators;

import javax.json.JsonNumber;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Трюк, что бы float/double значения выводились в одинаковом с Gson формате
 */
class FloatPointedClassWorkAround implements JsonNumber {

    @Override
    public boolean isIntegral() {
        return false;
    }

    @Override
    public int intValue() {
        return 0;
    }

    @Override
    public int intValueExact() {
        return 0;
    }

    @Override
    public long longValue() {
        return 0;
    }

    @Override
    public long longValueExact() {
        return 0;
    }

    @Override
    public BigInteger bigIntegerValue() {
        return null;
    }

    @Override
    public BigInteger bigIntegerValueExact() {
        return null;
    }

    @Override
    public double doubleValue() {
        return 0;
    }

    @Override
    public BigDecimal bigDecimalValue() {
        return null;
    }

    @Override
    public ValueType getValueType() {
        return ValueType.NUMBER;
    }
}
