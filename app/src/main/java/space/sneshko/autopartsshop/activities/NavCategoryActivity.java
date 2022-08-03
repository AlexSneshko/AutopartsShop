package space.sneshko.autopartsshop.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import space.sneshko.autopartsshop.R;
import space.sneshko.autopartsshop.adapters.NavCategoryDetailedAdapter;
import space.sneshko.autopartsshop.adapters.ViewAllAdapter;
import space.sneshko.autopartsshop.models.NavCategoryDetailedModel;
import space.sneshko.autopartsshop.models.PopularModel;
import space.sneshko.autopartsshop.models.ViewAllModel;

public class NavCategoryActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ProgressBar progressBar;
    List<NavCategoryDetailedModel> list;
    NavCategoryDetailedAdapter adapter;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_category);

        db = FirebaseFirestore.getInstance();
        String requiredType = getIntent().getStringExtra("type");

        recyclerView = findViewById(R.id.nav_cat_det_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setVisibility(View.GONE);
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);

        list = new ArrayList<>();
        adapter = new NavCategoryDetailedAdapter(this, list);
        recyclerView.setAdapter(adapter);

        db.collection("NavCategoryDetailed").whereEqualTo("type", requiredType)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                            NavCategoryDetailedModel navCategoryDetailedModel = documentSnapshot.toObject(NavCategoryDetailedModel.class);
                            list.add(navCategoryDetailedModel);
                            adapter.notifyDataSetChanged();

                            progressBar.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }
}