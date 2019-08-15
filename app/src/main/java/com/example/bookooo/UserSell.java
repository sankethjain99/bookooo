package com.example.bookooo;

public class UserSell {
    public String Name,BookName,AuthorName,Phonee,Sem,Branch;


    public UserSell() {
    }

    public UserSell(String name, String bookName, String authorName, String phonee, String sem, String branch) {
        Name = name;
        BookName = bookName;
        AuthorName = authorName;
        Phonee = phonee;
        Sem = sem;
        Branch = branch;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getBookName() {
        return BookName;
    }

    public void setBookName(String bookName) {
        BookName = bookName;
    }

    public String getAuthorName() {
        return AuthorName;
    }

    public void setAuthorName(String authorName) {
        AuthorName = authorName;
    }

    public String getPhonee() {
        return Phonee;
    }

    public void setPhonee(String phonee) {
        Phonee = phonee;
    }

    public String getSem() {
        return Sem;
    }

    public void setSem(String sem) {
        Sem = sem;
    }

    public String getBranch() {
        return Branch;
    }

    public void setBranch(String branch) {
        Branch = branch;
    }
}
