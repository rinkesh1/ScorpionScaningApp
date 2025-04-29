package in.webx.scorpion.Model;

import java.util.ArrayList;

public class StoreField {

    private ArrayList<Integer> imagelist;
    private ArrayList<String> imagenamelist;

    private ArrayList<Integer> color;

    public StoreField() {
    }
    public ArrayList<Integer> getColor() {
        return color;
    }

    public void setColor(ArrayList<Integer> color) {
        this.color = color;
    }

    public ArrayList<Integer> getImagelist() {
        return imagelist;
    }

    public void setImagelist(ArrayList<Integer> imagelist) {
        this.imagelist = imagelist;
    }

    public ArrayList<String> getImagenamelist() {
        return imagenamelist;
    }

    public void setImagenamelist(ArrayList<String> imagenamelist) {
        this.imagenamelist = imagenamelist;
    }

}
