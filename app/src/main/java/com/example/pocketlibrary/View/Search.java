package com.example.pocketlibrary.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.pocketlibrary.Model.BookModel;
import com.example.pocketlibrary.R;
import com.example.pocketlibrary.ViewModel.BooksRVAdapterSearch;
import com.example.pocketlibrary.databinding.ActivitySearchBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Search extends Fragment {
    ActivitySearchBinding binding;
    private RecyclerView newsRV;
    private ArrayList<BookModel> bookModelArrayList;
    private BooksRVAdapterSearch booksRVAdapter;
    private FirebaseFirestore db;

    public Search(){
    }

    public static Search newInstance() {
        return new Search();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_search, container, false);
        binding = ActivitySearchBinding.bind(view);

        newsRV = binding.searchRV;
        db = FirebaseFirestore.getInstance();
        bookModelArrayList = new ArrayList<>();
        booksRVAdapter = new BooksRVAdapterSearch(bookModelArrayList, getContext());
        newsRV.setHasFixedSize(true);
        newsRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        newsRV.setAdapter(booksRVAdapter);
        loadRecyclerViewData();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.search3.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                booksRVAdapter.filter(s);
                return false;
            }
        });
    }

    private void loadRecyclerViewData(){
        db.collection("Data").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()){
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot d : list){
                        BookModel bookModel = d.toObject(BookModel.class);
                        bookModelArrayList.add(bookModel);
                    }
                    booksRVAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "No data found in database", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Fail to get data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
