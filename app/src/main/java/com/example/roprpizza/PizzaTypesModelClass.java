package com.example.roprpizza;

public class PizzaTypesModelClass
{

    //Model class for data population
    private String pizzaName;
    private String imageOfPizza;

    public PizzaTypesModelClass(String pizzaName, String imageOfPizza)
    {
        this.pizzaName = pizzaName;
        this.imageOfPizza = imageOfPizza;
    }

    public String getPizzaName() {
        return pizzaName;
    }

    public String getImageOfPizza() {
        return imageOfPizza;
    }
}
