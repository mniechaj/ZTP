import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.ejb.Stateless;


@Stateless
public class NprimeBean implements NprimeRemote {

    public NprimeBean() {

    }

	/*
	 * Finds prime number that is equal or smaller than 'n' parameter
	 * and satisfies condition that return value is divisible by 4
	 *  with the rest of the division equals to 3
	 */
	@Override	
	public int prime(int n) {
		if (n >= 0) {
			int ret = 0;
			List<Integer> numbersToCheck = IntStream.rangeClosed(1, n).filter(num -> (num % 4 == 3)).boxed().collect(Collectors.toList());
			Collections.reverse(numbersToCheck);
			for (Integer i : numbersToCheck) {
				double upperBound = Math.sqrt(i);
				boolean isDivisible = true;
				int div = 3;
				while ((double)div <= upperBound) {
					if (i % div == 0) {
						isDivisible = false;
						break;
					}
					div += 2;
				}
				
				if (isDivisible) {
					ret = i;
					break;
				}
			}
			return ret;	
		} else {
			return 0;
		}
	}

}
