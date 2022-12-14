import java.util.Vector;


 class Point {
    int x;
    int y;

    public Point() {

    }
    public Point(int x, int y){
        this.x = x;
        this.y = y;
    }
    public Point(Point point) {
        this.x = point.x;
        this.y = point.y;
    }
}

public class Set {
    public String setName;
    public String varName;
    public boolean tri;
    Vector<Point> values = new Vector<>();
    public float mean;
    public float centroid;

    Range setRange = new Range();

    public Set(String setName, String varName, boolean tri, Vector<Integer> values)
    {
        this.setName=setName;
        this.varName=varName;
        this.tri= tri;
        Point p1 = new Point(values.get(0),0);
        Point p2 = new Point(values.get(1),1);
        this.values.add(p1);
        this.values.add(p2);

        if(!tri)
        {
            Point p3 = new Point(values.get(2),1);
            Point p4 = new Point(values.get(3),0);
            this.values.add(p3);
            this.values.add(p4);

        }
        else
        {
            Point p3 = new Point(values.get(2),0);
            this.values.add(p3);
        }
        setRange.start=values.get(0);
        setRange.end=values.get(values.size()-1);
    }
    public Set(String setName, String varName)
    {
        this.setName=setName;
        this.varName=varName;
    }
    public Set()
    {

    }

    public Set(Set s) {
        this.setName=s.setName;
        this.varName=s.varName;
        this.tri= s.tri;
        this.values = s.values;
        this.mean = s.mean;
    }
    // For output var
    public float calculateC(){
        float sum = 0;
        for (int i = 0; i < values.size(); i++) {
            sum += values.get(i).x;
        }
        centroid = sum/values.size();
        return sum/values.size();
    }
    @Override
    public String toString() {
        return "Set{" +
                "setName='" + setName + '\'' +
                ", varName='" + varName + '\'' +
                ", tri=" + tri +
                ", mean=" + mean +
                ", setRange=" + setRange +
                '}';
    }
}
