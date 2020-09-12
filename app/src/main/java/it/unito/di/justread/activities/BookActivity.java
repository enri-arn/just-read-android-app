package it.unito.di.justread.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.clans.fab.FloatingActionMenu;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import it.unito.di.justread.R;
import it.unito.di.justread.adapters.ReviewAdapter;
import it.unito.di.justread.adapters.TagAdapter;
import it.unito.di.justread.asynctasks.GetJsonTask;
import it.unito.di.justread.asynctasks.GetJsonTaskWithParameter;
import it.unito.di.justread.asynctasks.TaskCompletionListener;
import it.unito.di.justread.model.BookInfoData;
import it.unito.di.justread.model.Category;
import it.unito.di.justread.model.ReviewsData;

public class BookActivity extends AppCompatActivity implements TagAdapter.TagAdapterOnClickHandler, TaskCompletionListener<String>{

    private String BOOKINFO_URL = "http://192.168.0.1:8080/JustRead/api/catalog/book/";
    private String BOOKED_URL = "http://192.168.0.1:8080/JustRead/api/borrowing/book/";
    private String DELIVER_BOOK = "http://192.168.0.1:8080/JustRead/api/borrowing/";
    private String FAVOURITE_URL = "http://192.168.0.1:8080/JustRead/api/favourites/";
    private String ADD_REVIEW = "http://192.168.0.1:8080/JustRead/api/catalog/addreview/";
    private String INCREASELOAN = "http://192.168.0.1:8080/JustRead/api/borrowing/increase/";

    private ArrayList<Category> categories;
    private ArrayList<ReviewsData> reviews;
    private BookInfoData book;
    private RecyclerView tagRecyclerView;
    private RecyclerView reviewRecyclerView;
    private RelativeLayout personalReview;
    private ReviewAdapter adapter1;
    private TagAdapter adapter;
    private FloatingActionMenu fabMenu;
    private com.github.clans.fab.FloatingActionButton fabBook, fabSendHome;
    private ImageButton showMore;
    private ImageButton showLess;
    private ImageView bookCollapsingCover;
    private ImageView profileImage;
    private TextView plotText;
    private TextView rateText;
    private TextView authorText;
    private TextView bookTitle;
    private TextView publisherBook;
    private TextView pagesBook;
    private TextView isbnBook;
    private TextView numberVoters;
    private TextView languageBook;
    private Button confirmReview;
    private EditText reviewText;
    private RatingBar reviewRate;
    private RatingBar ratingBar;
    private String title = null;
    private ProgressBar fiveStar;
    private ProgressBar fourStar;
    private ProgressBar threeStar;
    private ProgressBar twoStar;
    private ProgressBar oneStar;

    boolean isFisrt = false;
    boolean isFavourite = false;
    boolean isBorrowed = false;

    private String isbn = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null){
            isbn = bundle.getString("isbn");
        }

        book = new BookInfoData();
        reviews = new ArrayList<>();
        categories = new ArrayList<>();

        String a = BOOKINFO_URL += isbn;
        Log.i("EXCEPTION", "book activity URL = " + a);
        GetJsonTask getJsonTask = new GetJsonTask("GET", this);
        getJsonTask.execute(a);

        List<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("info", "categories"));
        List<NameValuePair> nameValuePairs1 = new ArrayList<>();
        nameValuePairs1.add(new BasicNameValuePair("info", "reviews"));

        GetJsonTaskWithParameter getJsonCategory = new GetJsonTaskWithParameter("POST", new TaskCompletionListener<String>() {
            @Override
            public void onComplete(String result) {
                if (result != null){
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        List<Category> categoryList = mapper.readValue(result, new TypeReference<List<Category>>() {});
                        categories.addAll( categoryList );
                        adapter.notifyDataSetChanged();
                    } catch (Exception e){
                        Log.i("EXCEPTION", "Error parsing json category");
                    }
                }
            }
        }, nameValuePairs);
        getJsonCategory.execute(BOOKINFO_URL);

        GetJsonTaskWithParameter getJsonReviews = new GetJsonTaskWithParameter("POST", new TaskCompletionListener<String>() {
            @Override
            public void onComplete(String result) {
                if (result != null){
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        List<ReviewsData> reviewsList = mapper.readValue(result, new TypeReference<List<ReviewsData>>() {});
                        reviews.addAll( reviewsList );
                        adapter1.notifyDataSetChanged();
                    } catch (Exception e){
                        Log.i("EXCEPTION", "Error parsing json review");
                    }
                }
            }
        }, nameValuePairs1);
        getJsonReviews.execute(BOOKINFO_URL);

        authorText = findViewById(R.id.book_author_info_book);
        bookTitle = findViewById(R.id.book_title_info_book);
        bookCollapsingCover = findViewById(R.id.book_collapsing_cover);
        numberVoters = findViewById(R.id.num_voters);
        rateText = findViewById(R.id.rate_book_info);
        ratingBar = findViewById(R.id.ratingBar);
        fiveStar = findViewById(R.id.fiveStar);
        fourStar = findViewById(R.id.fourStar);
        threeStar = findViewById(R.id.threeStar);
        twoStar = findViewById(R.id.twoStar);
        oneStar = findViewById(R.id.oneStar);
        showMore = findViewById(R.id.button_show_more);
        showLess = findViewById(R.id.button_show_less);
        plotText = findViewById(R.id.plot_text);
        publisherBook = findViewById(R.id.default_publisher_lbl_book);
        pagesBook = findViewById(R.id.default_pages_lbl_book);
        isbnBook = findViewById(R.id.default_isbn_lbl_book);
        languageBook = findViewById(R.id.default_language_lblb_book);
        confirmReview = findViewById(R.id.confirm_button_review);
        reviewText = findViewById(R.id.editText_review);
        reviewRate = findViewById(R.id.ratingBar_review);
        personalReview = findViewById(R.id.personal_review);
        profileImage  = findViewById(R.id.profile_image_review);
        fabMenu = findViewById(R.id.fab_menu);
        fabMenu.setClosedOnTouchOutside(true);
        fabBook = findViewById(R.id.fab_book_item);
        fabSendHome = findViewById(R.id.fab_send_home_item);
        tagRecyclerView = findViewById(R.id.genre_recyclerview);
        tagRecyclerView.setHasFixedSize(true);
        reviewRecyclerView = findViewById(R.id.recyclerview_review);
        reviewRecyclerView.setHasFixedSize(true);

        adapter = new TagAdapter(categories, this);
        tagRecyclerView.setAdapter(adapter);

        adapter1 = new ReviewAdapter(reviews, getApplicationContext());
        reviewRecyclerView.setAdapter(adapter1);
        reviewRecyclerView.setNestedScrollingEnabled(false);

        final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.toolbar_layout);
        AppBarLayout appBarLayout = findViewById(R.id.app_bar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle(title);
                    isShow = true;
                } else if(isShow) {
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                }
            }
        });

        final RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) personalReview.getLayoutParams();
        confirmReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFisrt){
                    boolean a = addReview(reviewText.getText().toString(), reviewRate.getRating());
                    if (a) {
                        confirmReview.setText(R.string.modified_review);
                        params.height = 0;
                        personalReview.setLayoutParams(params);
                    }
                } else {
                    params.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
                    personalReview.setLayoutParams(params);
                    modifyReview(reviewText.getText().toString(), reviewRate.getRating());
                    confirmReview.setText(R.string.default_confirm);
                }
                Snackbar.make(getCurrentFocus(), "Review added", BaseTransientBottomBar.LENGTH_SHORT).show();
            }
        });

        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFavourite){
                    addToFavourite();
                }
            }
        });

        showMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RelativeLayout.LayoutParams paramsPlotText = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                plotText.setLayoutParams(paramsPlotText);
                showMore.setVisibility(View.INVISIBLE);
                showMore.setFocusable(false);
                showLess.setVisibility(View.VISIBLE);
                showLess.setFocusable(true);
            }
        });
        showLess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RelativeLayout.LayoutParams paramsPlotText = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 600);
                plotText.setLayoutParams(paramsPlotText);
                showLess.setVisibility(View.INVISIBLE);
                showLess.setFocusable(false);
                showMore.setVisibility(View.VISIBLE);
                showMore.setFocusable(true);
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManagerVertical = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        tagRecyclerView.setLayoutManager(layoutManager);
        reviewRecyclerView.setLayoutManager(layoutManagerVertical);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            onBackPressed();
        }
        /*TODO aggiungere share!!
        else if (id == R.id.action_share){
            Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                    .setType("text/plain")
                    .setText("Sto leggendo un libro!" + "#JUST_READ #PoveriSenzaApp #IoCeLho")
                    .getIntent();
            startActivity(shareIntent);
        }*/
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View source, int position) {
        Intent intent = new Intent(source.getContext(), GenreActivity.class);
        intent.putExtra("title", categories.get(position).getName());
        startActivity(intent);
    }

    public boolean addReview(final String text, final Float rate){
        final boolean[] added = {false};
        String url = ADD_REVIEW + isbn;
        List<NameValuePair> list = new ArrayList<>();
        list.add(new BasicNameValuePair("review", text));
        list.add(new BasicNameValuePair("rate", String.valueOf(rate)));
        list.add(new BasicNameValuePair("littleHeart", String.valueOf(true)));
        GetJsonTaskWithParameter addReview = new GetJsonTaskWithParameter("POST", new TaskCompletionListener<String>() {
            @Override
            public void onComplete(String result) {
                if (result != null){
                    if (result.equalsIgnoreCase("true")){
                        added[0] = true;
                    }
                }
                else{
                    added[0] = false;
                }
            }
        }, list);
        addReview.execute(url);
        return added[0];
    }

    public boolean modifyReview(String text, Float rate){
        reviews.remove(0);
        isFisrt = false;
        final boolean[] added = {false};
        String url = ADD_REVIEW + isbn;

        return added[0];
    }

    public void addToFavourite(){
        String favouriteUrl = FAVOURITE_URL + isbn;
        GetJsonTask addToFavourite = new GetJsonTask("PUT" ,new TaskCompletionListener<String>() {
            @Override
            public void onComplete(final String result) {
                if (result.equalsIgnoreCase("true")){
                    isFavourite = true;
                    Snackbar.make(getCurrentFocus(), "Book added to your favourite list", Snackbar.LENGTH_LONG).show();
                } else {
                    isFavourite = false;
                    Snackbar.make(getCurrentFocus(), "Book has not been added, we invite you to try again", Snackbar.LENGTH_LONG).show();
                }
            }
        });
        addToFavourite.execute(favouriteUrl);
    }

    public void increaseLoan(){
        String increaseUrl = INCREASELOAN + isbn;
        GetJsonTask increaseLoan = new GetJsonTask("PUT", new TaskCompletionListener<String>() {
            @Override
            public void onComplete(String result) {
                if (result.equalsIgnoreCase("true")) {
                    Snackbar.make(getCurrentFocus(), "Book will be increase", Snackbar.LENGTH_LONG).show();
                } else {
                    String text = "Operation wasn't successful, we invite you to try again";
                    Snackbar.make(getCurrentFocus(), text, Snackbar.LENGTH_LONG).show();
                }
            }
        });
        increaseLoan.execute(increaseUrl);
    }

    public void bookTheBook(final int sentHome){
        String bookedurl = BOOKED_URL + isbn;
        final GetJsonTask bookedTask = new GetJsonTask( "PUT" , new TaskCompletionListener<String>() {
            @Override
            public void onComplete(String result) {
                if (result.equalsIgnoreCase("true")) {
                    String text = sentHome == 0 ? "Booked book" : "Book will  be sent home";
                    Snackbar.make(getCurrentFocus(), text, Snackbar.LENGTH_LONG)
                            .setAction("UNDO", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    deliverBorrow(sentHome);
                                }
                            }).show();
                } else {
                    String text = "Book is not avaiable now";
                    Snackbar.make(getCurrentFocus(), text, Snackbar.LENGTH_LONG).show();
                }
            }
        });
        bookedTask.execute(bookedurl);
    }

    public void deliverBorrow(final int sentHome){
        String deliverBook = DELIVER_BOOK + isbn;
        final GetJsonTask bookedTask = new GetJsonTask("DELETE", new TaskCompletionListener<String>() {
            @Override
            public void onComplete(String result) {
                if (result.equalsIgnoreCase("true")) {
                    Snackbar.make(getCurrentFocus(), "Booked book cancelled", Snackbar.LENGTH_LONG)
                            .setAction("UNDO", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    bookTheBook(sentHome);
                                }
                            }).show();
                }
            }
        });
        bookedTask.execute(deliverBook);
    }

    @Override
    public void onComplete(String result) {
        if (result != null) {
            try {
                Log.i("EXCEPTION", "book activity result = " + result);
                ObjectMapper mapper = new ObjectMapper();
                book = mapper.readValue(result, BookInfoData.class);
                Log.i("EXCEPTION", book.toString());
                bookTitle.setText(book.getTitle());
                authorText.setText(book.getAuthor());
                plotText.setText(book.getPlot());
                publisherBook.setText(book.getPublisher());
                pagesBook.setText(String.valueOf(book.getPages()));
                isbnBook.setText(book.getIsbn());
                languageBook.setText(book.getLanguage());
                isBorrowed = book.isIsAvaiable();
                rateText.setText(String.valueOf(book.getRate()));
                ratingBar.setRating(book.getRate());
                float voters = book.getNumVoters();
                numberVoters.setText(String.valueOf(voters));
                fiveStar.setProgress((int) (book.getFiveStar()  / voters * 100));
                fourStar.setProgress((int) (book.getFourStar() / voters * 100));
                threeStar.setProgress((int) (book.getThreeStar() / voters * 100));
                twoStar.setProgress((int) (book.getTwoStar() / voters * 100));
                oneStar.setProgress((int) (book.getOneStar() / voters * 100));
                Picasso.with(this).load(book.getCover()).fit().centerCrop().into(bookCollapsingCover);
                if (!isBorrowed){
                    fabBook.setImageResource(R.drawable.ic_loop_white_24dp);
                    fabBook.setLabelText(getString(R.string.fab_extend_loan));
                    fabSendHome.setImageResource(R.drawable.ic_event_available_white_24dp);
                    fabSendHome.setLabelText(getString(R.string.fab_delivery_book));
                }
                fabBook.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (isBorrowed) {
                            bookTheBook(0);
                        } else {
                            increaseLoan();
                        }
                    }
                });
                fabSendHome.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (isBorrowed) {
                            bookTheBook(1);
                        } else {
                            deliverBorrow(0);
                        }
                    }
                });
            } catch (Exception e) {
                Log.i("EXCEPTION", "Error parsing json book activity");
            }
        } else {
            Intent goToOption = new Intent(getApplicationContext(), ServerOfflineActivity.class);
            startActivity(goToOption);
            finish();
        }
    }
}
