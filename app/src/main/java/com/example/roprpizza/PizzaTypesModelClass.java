package com.example.roprpizza;

public class PizzaTypesModelClass
{

    //Model class for data population
    private String pizzaName;
    private int imageOfPizza;

    public PizzaTypesModelClass(String pizzaName, int imageOfPizza)
    {
        this.pizzaName = pizzaName;
        this.imageOfPizza = imageOfPizza;
    }

    public String getPizzaName()
    {
        return pizzaName;
    }

    public void setPizzaName(String pizzaName)
    {
        this.pizzaName = pizzaName;
    }

    public int getImageOfPizza()
    {
        return imageOfPizza;
    }

    public void setImageOfPizza(int imageOfPizza)
    {
        this.imageOfPizza = imageOfPizza;
    }
}
