package com.example.pocketlibrary.View;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.pocketlibrary.Model.BookModel;
import com.example.pocketlibrary.R;
import com.example.pocketlibrary.ViewModel.AuthViewModel;
import com.example.pocketlibrary.ViewModel.BooksRVAdapterFAV;
import com.example.pocketlibrary.ViewModel.UserViewModel;
import com.example.pocketlibrary.databinding.FragmentBooksListBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class BooksList extends Fragment {
    FragmentBooksListBinding binding;
    private RecyclerView newsRV;
    private ArrayList<BookModel> bookModelArrayList;
    private BooksRVAdapterFAV booksRVAdapter;
    private FirebaseFirestore db;
    private UserViewModel UVM = new UserViewModel();

    public BooksList() {
        // Required empty public constructor
    }

    public static BooksList newInstance() {
        return new BooksList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_books_list, container, false);
        binding = FragmentBooksListBinding.bind(view);

        newsRV = binding.container1;
        db = FirebaseFirestore.getInstance();
        bookModelArrayList = new ArrayList<>();
        booksRVAdapter = new BooksRVAdapterFAV(bookModelArrayList, getContext());
        newsRV.setHasFixedSize(true);
        newsRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        newsRV.setAdapter(booksRVAdapter);
        UVM.setData(AuthViewModel.giveUser());
        UVM.getmUser().observe(getViewLifecycleOwner(), user -> {
            String userName = user.getNickname();
            Log.d("BooksList", "User name: " + userName);
            loadRecyclerViewData(userName);
        });
        return binding.getRoot();
    }

    private void loadRecyclerViewData(String name) {
        Log.d("BooksList", "Loading data for user: " + name);
        db.collection("Data").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                Log.d("BooksList", "Firestore query success");
                if (!queryDocumentSnapshots.isEmpty()) {
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot d : list) {
                        BookModel bookModel = d.toObject(BookModel.class);
                        bookModelArrayList.add(bookModel);
                    }
                    booksRVAdapter.notifyDataSetChanged();
                } else {
                    Log.d("BooksList", "No documents found in collection 'Data'");
                    Toast.makeText(getContext(), "No data found in database", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(e -> {
            Log.e("Firestore", "Query failed: " + e.getMessage());
        });
    }
}