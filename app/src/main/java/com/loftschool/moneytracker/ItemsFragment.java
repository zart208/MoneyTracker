package com.loftschool.moneytracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.loftschool.moneytracker.api.AddItemResult;
import com.loftschool.moneytracker.api.LSApi;
import com.loftschool.moneytracker.api.LogoutResult;
import com.loftschool.moneytracker.api.RemoveResult;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.loftschool.moneytracker.Item.TYPE_UNKNOWN;

public class ItemsFragment extends Fragment {
    public static final int RC_CONFIRM = 111;
    private static final String KEY_TYPE = "TYPE";
    RecyclerView recyclerView;
    FloatingActionButton fab;
    SwipeRefreshLayout refreshLayout;
    private List<Item> items;
    private List<Integer> itemsForRemove;
    private String type = TYPE_UNKNOWN;
    private ItemsAdapter adapter;
    private LSApi api;
    private ActionMode actionMode;
    private ActionMode.Callback actionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.items_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_remove:
                    itemsForRemove = adapter.getSelectedItems();
                    showDialog();
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            adapter.clearSelections();
            actionMode = null;
            fab.show();
        }
    };

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
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        fab = view.findViewById(R.id.fab_add);
        recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        refreshLayout = view.findViewById(R.id.refresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadItems();
            }
        });
        adapter.setListener(new ItemsAdapterListener() {
            @Override
            public void onItemClick(Item item, int position) {
                if (isInActionMode()) {
                    if (adapter.getSelectedItemCount() == 1
                            && adapter.getSelectedItems().contains(new Integer(position))) {
                        actionMode.finish();
                        return;
                    }
                    toggleSelection(position);
                }
            }

            @Override
            public void onItemLongClick(Item item, int position) {
                if (isInActionMode()) {
                    return;
                }
                actionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(actionModeCallback);
                fab.hide();
                toggleSelection(position);
            }

            private void toggleSelection(int position) {
                adapter.toggleSelection(position);
                actionMode.setTitle(view.getContext().getString(R.string.action_title, adapter.getSelectedItemCount()));
            }

            private boolean isInActionMode() {
                return actionMode != null;
            }
        });

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
                if (response.isSuccessful()) {
                    items = new ArrayList<>();
                    items.addAll(response.body());
                    adapter.setItems(items);
                    recyclerView.getAdapter().notifyDataSetChanged();
                    refreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                t.getMessage();
            }
        });
    }

    private void addItem(final Item item) {
        api.add(item.name, item.price, item.type).enqueue(new Callback<AddItemResult>() {
            @Override
            public void onResponse(Call<AddItemResult> call, Response<AddItemResult> response) {
                if (response.isSuccessful()) {
                    loadItems();
                }
            }

            @Override
            public void onFailure(Call<AddItemResult> call, Throwable t) {
                t.getMessage();
            }
        });
    }

    private void remove() {
        for (int i = 0; i < itemsForRemove.size(); i++) {
            final int position = itemsForRemove.get(i);
            api.remove(items.get(position).id).enqueue(new Callback<RemoveResult>() {
                @Override
                public void onResponse(Call<RemoveResult> call, Response<RemoveResult> response) {

                }

                @Override
                public void onFailure(Call<RemoveResult> call, Throwable t) {
                    t.getMessage();
                }
            });
        }
    }

    private void logout() {
        api.logout().enqueue(new Callback<LogoutResult>() {
            @Override
            public void onResponse(Call<LogoutResult> call, Response<LogoutResult> response) {

            }

            @Override
            public void onFailure(Call<LogoutResult> call, Throwable t) {
                t.getMessage();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AddActivity.RC_ADD_ITEM && resultCode == RESULT_OK) {
            Item item = (Item) data.getSerializableExtra(AddActivity.RESULT_ITEM);
            addItem(item);
        } else if (requestCode == RC_CONFIRM && resultCode == RESULT_OK) {
            remove();
            okClicked();
        } else if (requestCode == RC_CONFIRM && resultCode == RESULT_CANCELED) {
            cancelClicked();
        }
    }

    private void removeSelectedItems() {
        for (int i = adapter.getSelectedItems().size() - 1; i >= 0; i--) {
            adapter.remove(adapter.getSelectedItems().get(i));
        }
    }

    private void showDialog() {
        DialogFragment dialog = new ConfirmationDialog();
        dialog.setTargetFragment(this, RC_CONFIRM);
        dialog.show(getFragmentManager(), "Confirmation");
    }

    public void okClicked() {
        removeSelectedItems();
        actionMode.finish();
    }

    public void cancelClicked() {
        actionMode.finish();
    }
}