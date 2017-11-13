package com.loftschool.moneytracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.loftschool.moneytracker.api.LSApi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.loftschool.moneytracker.Item.TYPE_UNKNOWN;

public class ItemsFragment extends Fragment {
    public static final int LOADER_ITEMS = 0;
    public static final int LOADER_ADD_ITEMS = 1;
    private static final String KEY_TYPE = "TYPE";
    RecyclerView recyclerView;
    private String type = TYPE_UNKNOWN;
    private ItemsAdapter adapter;
    private LSApi api;

    public static ItemsFragment createItemsFragment(String type) {
        Bundle bundle = new Bundle();
        bundle.putString(ItemsFragment.KEY_TYPE, type);
        ItemsFragment fragment = new ItemsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getArguments().getString(KEY_TYPE, TYPE_UNKNOWN);
        if (type.equals(TYPE_UNKNOWN)) {
            throw new IllegalStateException("Unknown fragment type");
        }
        adapter = new ItemsAdapter(getContext());
        api = ((App) getActivity().getApplication()).getApi();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_items, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        FloatingActionButton fab = view.findViewById(R.id.fab_add);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddActivity.class);
                intent.putExtra(AddActivity.EXTRA_TYPE, type);
                startActivityForResult(intent, AddActivity.RC_ADD_ITEM);
            }
        });
        loadItems();
    }

    private void loadItems() {
        api.items(type).enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                List<Item> items = new ArrayList<>();
                items.addAll(response.body());
                adapter.setItems(items);
                recyclerView.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

//    private void loadItems() {
//        getLoaderManager().initLoader(LOADER_ITEMS, null, new LoaderManager.LoaderCallbacks<List<Item>>() {
//
//            @Override
//            public Loader<List<Item>> onCreateLoader(int id, Bundle args) {
//                return new AsyncTaskLoader<List<Item>>(getContext()) {
//                    @Override
//                    public List<Item> loadInBackground() {
//                        List<Item> items;
//                        try {
//                            items = api.items(type).execute().body();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                            return null;
//                        }
//                        return items;
//                    }
//                };
//            }
//
//            @Override
//            public void onLoadFinished(Loader<List<Item>> loader, List<Item> items) {
//                if (items == null) {
//                    showError(getString(R.string.error_loading_items_text));
//                } else {
//                    adapter.setItems(items);
//                }
//            }
//
//            @Override
//            public void onLoaderReset(Loader<List<Item>> loader) {
//
//            }
//        }).forceLoad();
//    }

    private void showError(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }

    private void addItem(final Item item) {
        getLoaderManager().initLoader(LOADER_ADD_ITEMS, null, new LoaderManager.LoaderCallbacks() {

            @Override
            public Loader onCreateLoader(int id, Bundle args) {
                return new AsyncTaskLoader(getContext()) {
                    @Nullable
                    @Override
                    public Object loadInBackground() {
                        try {
                            return api.add(item.id, item.name, item.price, item.type).execute().body();
                        } catch (IOException e) {
                            showError(e.getMessage());
                            return null;
                        }
                    }
                };
            }

            @Override
            public void onLoadFinished(Loader loader, Object item) {
                if (item == null) {
                    showError(getString(R.string.erorr_item_adding_text));
                } else {
                    Toast.makeText(getContext(), R.string.item_adding_text, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onLoaderReset(Loader loader) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AddActivity.RC_ADD_ITEM && resultCode == RESULT_OK) {
            Item item = (Item) data.getSerializableExtra(AddActivity.RESULT_ITEM);
            Toast.makeText(getContext(), item.name + " " + item.price, Toast.LENGTH_LONG).show();
        }
    }
}