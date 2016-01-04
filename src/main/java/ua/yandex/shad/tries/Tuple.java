package ua.yandex.shad.tries;

public class Tuple {
    private final String term;
    private final int weight;
    
    public Tuple() {
        term = "";
        weight = 0;
    }
    
    public Tuple(String term, int weight) {
        this.term = term;
        this.weight = weight;
    }  
    
    public int getWeight() {
        return weight;
    }

    public String getTerm() {
        return term;
    }
    
    @Override 
    public boolean equals(Object obj) {
        if (obj == null) {
                return false;
        }
        if (!(obj instanceof Tuple)) {
            return false;
        }    
        Tuple other = (Tuple) obj;
        boolean eqTerm = false;
        if (term == null) {
            eqTerm = other.term == null;
        } else {
            eqTerm = term.equals(other.term);
        }
        return eqTerm && weight == other.weight;   
    }
    
    @Override
    public int hashCode() {
        return 42;
    }    
}
