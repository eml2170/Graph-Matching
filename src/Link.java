package dc2;

import java.util.HashSet;
import java.util.Set;

public class Link implements Comparable{

	String u;
	String v;
	
	Set<Link> witnesses;
	int score;
	
	public Link(String u ,String v){
		this.u = u;
		this.v = v;
		
		score = 0;
		witnesses = new HashSet<Link>();
	}

	@Override
	public String toString() {
		return "Link [u=" + u + ", v=" + v + ", score=" + score + "]";
	}

	@Override
	public int compareTo(Object o) {
		Link otherLink = (Link) o;
		if(this.score < otherLink.score)
			return 1;
		if(this.score > otherLink.score)
			return -1;
		else return 0;
	}
	
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((u == null) ? 0 : u.hashCode());
		result = prime * result + ((v == null) ? 0 : v.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Link other = (Link) obj;
		if (u == null) {
			if (other.u != null)
				return false;
		} else if (!u.equals(other.u))
			return false;
		if (v == null) {
			if (other.v != null)
				return false;
		} else if (!v.equals(other.v))
			return false;
		return true;
	}

	private void update(){
		score = witnesses.size();
	}
	
	public void add(Link link){
		witnesses.add(link);
		update();
	}
}
