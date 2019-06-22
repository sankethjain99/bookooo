package com.example.bookooo;

import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.bookooo.Database.Database;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class BookDetail extends AppCompatActivity {

    TextView book_name,book_price,book_description;
    ImageView book_image;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart;
    ElegantNumberButton numberButton;


    String bookId="";

    FirebaseDatabase database;
    DatabaseReference book;

    Book currentBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        database = FirebaseDatabase.getInstance();
        book = database.getReference("Book");

        numberButton = (ElegantNumberButton)findViewById(R.id.number_button);
        btnCart = (FloatingActionButton)findViewById(R.id.btnCart);

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Database(getBaseContext()).addToCart(new Order(
                        bookId,
                        currentBook.getName(),
                        numberButton.getNumber(),
                        currentBook.getPrice(),
                        currentBook.getDiscount()

                ));

                Toast.makeText(BookDetail.this, "Added To Cart", Toast.LENGTH_SHORT).show();
            }
        });

        book_description = (TextView)findViewById(R.id.book_description);
        book_name = (TextView)findViewById(R.id.book_name);
        book_price = (TextView)findViewById(R.id.book_price);
        book_image = (ImageView)findViewById(R.id.img_book);


        collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);


        if (getIntent() != null)
            bookId = getIntent().getStringExtra("BookId");
        if (!bookId.isEmpty()){
            getDetailBook(bookId);
        }
    }

    private void getDetailBook(String bookId) {
        book.child(bookId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentBook = dataSnapshot.getValue(Book.class);

                Picasso.with(getBaseContext()).load(currentBook.getImage()).into(book_image);

                collapsingToolbarLayout.setTitle(currentBook.getName());

                book_price.setText(currentBook.getPrice());

                book_name.setText(currentBook.getName());

                book_description.setText(currentBook.getDescription());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
