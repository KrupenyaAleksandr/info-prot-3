package education.infoprotection;

public class Divergence implements Comparable<Divergence>{

    private String key;
    private double divergence;

    Divergence(String key, double divergence) {
        this.key = key;
        this.divergence = divergence;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public double getDivergence() {
        return divergence;
    }

    public void setDivergence(double divergence) {
        this.divergence = divergence;
    }

    @Override
    public int compareTo(Divergence o) {
        return Double.compare(this.divergence, o.getDivergence());
    }
}
