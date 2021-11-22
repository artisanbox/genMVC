package userDemo.model;

import genMVC.model.BaseModel;
import genMVC.model.Table;
import sun.text.normalizer.Utility;

import java.util.Scanner;

@Table(name = "book", primaryKey = "bId")
public class BookModel extends BaseModel {
    public Integer bId;
    public String author;
    public Float price;
}
