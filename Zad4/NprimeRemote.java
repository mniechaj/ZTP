import javax.ejb.Remote;

@Remote
public interface NprimeRemote {
	
	int prime(int n);
}
