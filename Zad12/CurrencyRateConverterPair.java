/**
 * Auxiliary immutable class that represents pair of
 * currency rate and converter values
 *
 * @author Maciek Niechaj
 */
public class CurrencyRateConverterPair {
    
    private final Double rate;
    private final Integer converter;

    public CurrencyRateConverterPair(Double rate, Integer converter) {
        this.rate = rate;
        this.converter = converter;
    }
    
    public Double getRate() {
        return rate;
    }

    public Integer getConverter() {
        return converter;
    }
    
    public Double getRateRatio(CurrencyRateConverterPair that) {
        return this.getRate() / that.getRate();
    }
    
    public Double getConverterRatio(CurrencyRateConverterPair that) {
        return 1.0 * this.getConverter() / that.getConverter();
    }
    
}
