/**
 * Resource class which contains the name and quantity of a resource
 * examples : water, food, wood, stone, etc.
 */
public class Resource {

    /** resource name */
    private String name;

    /** resource quantity */
    private int quantity;

    /** resource weight in grams  */
    private int weightInGrams;



    /**
     * Constructor by default
     */
    public Resource() {
        this.name = "default";
        this.quantity = 0;
        this.weightInGrams = 0;
    }

    /**
     * Constructor for Resource class
     * @param name
     * @param quantity
     */
    public Resource(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
        this.weightInGrams = 0;
    }

    /**
     * Constructor for Resource class
     * @param name
     * @param quantity
     * @param weightInGrams
     */
    public Resource(String name, int quantity, int weightInGrams) {
        this.name = name;
        this.quantity = quantity;
        this.weightInGrams = weightInGrams;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity < 0) {
            System.out.println("Quantity cannot be negative");
        } else {
            this.quantity = quantity;
        }
        this.quantity = quantity;
    }

    public int getWeightInGrams() {
        return weightInGrams;
    }

    public void setWeightInGrams(int weightInGrams) {
        this.weightInGrams = weightInGrams;
    }

    /**
     * Split the resource in tow by default or by a given quantity which will
     * update the current resource quantity and create a new resource with the new quantity.
     * @param quantity define the quantity of the resource to split, by default it's 2
     * @return the new resource created
     */
    public Resource Split(int quantity) {
        if (quantity < 0) {
            System.out.println("Quantity cannot be negative");
            return null;
        }
        else if (quantity == 0) {
            int quantityDiff = this.quantity % 2;
            if (quantityDiff == 0) {
                this.quantity = this.quantity / 2;
                Resource newResource = new Resource(this.name, this.quantity, this.weightInGrams);
                return newResource;
            } else {
                this.quantity = (this.quantity - quantityDiff) / 2 ;
                Resource newResource = new Resource(this.name, this.quantity, this.weightInGrams);
                this.quantity += quantityDiff;
                return newResource;
            }
        } else if (quantity < this.quantity) {
            this.quantity -= quantity;
            Resource newResource = new Resource(this.name, quantity, this.weightInGrams);
            return newResource;
        }
        return null;
    }
}
