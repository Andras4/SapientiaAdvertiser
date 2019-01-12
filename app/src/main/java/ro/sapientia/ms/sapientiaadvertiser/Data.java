package ro.sapientia.ms.sapientiaadvertiser;

public class Data {

    private String Text, Description;
    private int Image;

    public Data() {

    }

    public Data(String text, String description, int image) {
        Text = text;
        Description = description;
        Image = image;
    }

    public String getText() {
        return Text;
    }

    public int getImage() {
        return Image;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setText(String text) {
        Text = text;
    }

    public void setImage(int image) {
        Image = image;
    }
}
