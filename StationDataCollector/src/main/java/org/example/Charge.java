package org.example;

public class Charge {

    private int id;

    private int kwH;

    private int customer_id;

    public Charge(int id, int kwH, int customer_id) {
        this.id = id;
        this.kwH = kwH;
        this.customer_id = customer_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getKwH() {
        return kwH;
    }

    public void setKwH(int kwH) {
        this.kwH = kwH;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    @Override
    public String toString() {
        return "Charge{" +
                "id=" + id +
                ", kwH=" + kwH +
                ", customer_id=" + customer_id +
                '}';
    }
}
