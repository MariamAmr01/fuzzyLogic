import java.util.Vector;

class Range {
    public int start;
    public int end;

    public Range(int start, int end) {
        this.start = start;
        this.end = end;
    }
    public Range()
    {

    }
    public Range(Range r) {
        this.start = r.start;
        this.end = r.end;
    }

    public boolean contains(float number) {
        return (number >= start && number <= end);
    }

    @Override
    public String toString() {
        return "Range{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }
}

public class Variable {
    public String name;
    public Range range;
    public boolean input;
    public float crispVal;
    public Vector<Set> sets;

    public Variable(String name, Range range, boolean input, int crispVal)
    {
        this.name = name;
        this.range = new Range(range);
        this.sets = new Vector<>();
        this.input = input;
        this.crispVal = crispVal;
    }
    public Variable()
    {

    }

    public int findSet(String name)
    {
        for (int i = 0; i < sets.size(); i++) {
            if(sets.get(i).setName.equals(name))
                return i;
        }
        return -1;
    }

    public void setMean()
    {
        for (int i = 0; i < sets.size(); i++) {

            Vector<Point> points = new Vector<>();
            if(sets.get(i).setRange.contains(crispVal))
            {
                Range range1= new Range(sets.get(i).values.get(0).x,sets.get(i).values.get(1).x);
                Range range2= new Range(sets.get(i).values.get(1).x,sets.get(i).values.get(2).x);
                if(range1.contains(crispVal)){
                    points.add(sets.get(i).values.get(0));
                    points.add(sets.get(i).values.get(1));
                }
                else if(range2.contains(crispVal))
                {
                    points.add(sets.get(i).values.get(1));
                    points.add(sets.get(i).values.get(2));
                }
                if (!sets.get(i).tri) {
                    Range range3 = new Range(sets.get(i).values.get(2).x, sets.get(i).values.get(3).x);
                    if (range3.contains(crispVal)) {
                        points.add(sets.get(i).values.get(2));
                        points.add(sets.get(i).values.get(3));
                    }
                }
                sets.get(i).mean=calcSlope(points);

            }
            else
            {
                sets.get(i).mean=0;
            }
            //System.out.println(sets.get(i).mean);

        }


    }

    public float calcSlope(Vector<Point> points)
    {
        if(points.size()==0) return -1;
        if(points.get(0).x==crispVal)
        {
            return points.get(0).y;
        }
        if(points.get(1).x==crispVal)
        {
            return points.get(1).y;
        }

        if(points.get(0).x==points.get(1).x) return 1;
        if(points.get(0).y==points.get(1).y) return 0;

        float slope = (float)(points.get(1).y-points.get(0).y)/(points.get(1).x-points.get(0).x);
        float c;
        if(points.get(0).y==0)
            c = -(slope*points.get(0).x);
        else
            c = -(slope*points.get(1).x);
        return slope*crispVal+c;
    }
    @Override
    public String toString() {
        return "Variable{" +
                "name='" + name + '\'' +
                ", range=" + range +
                ", input=" + input +
                ", crispVal=" + crispVal +
                ", sets=" + sets +
                '}';
    }
}
