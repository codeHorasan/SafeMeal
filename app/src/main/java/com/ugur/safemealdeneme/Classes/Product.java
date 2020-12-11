import java.util.List;

public class Product {

    private int productId;
    private String name, description, image;
    private double price;
    private List<String> imageList;

    public Product() {
    }

    public Product(int productId, String name, String description, double price, List<String> imageList) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageList = imageList;
    }

    public Product( String name, String image, double price) {
        this.name = name;
        this.image = image;
        this.price = price;
    }

   

    public int getBasketID() {
        return basketID;
    }

    public void setBasketID(int basketID) {
        this.basketID = basketID;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<String> getImageList() {
        return imageList;
    }

    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
    }
}