package com.example.yourservices.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.example.yourservices.Injector;
import com.example.yourservices.R;
import com.example.yourservices.authenticator.LogoutService;
import com.example.yourservices.model.DiscountOffer;
import com.github.kevinsawicki.wishlist.SingleTypeAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import lombok.AccessLevel;
import lombok.Getter;

import static com.example.yourservices.core.Constants.Extra.DISCOUNT_OFFER_ITEM;

public class DiscountListFragment extends ItemListFragment<DiscountOffer> {

//    @Inject
//    protected BootstrapServiceProvider serviceProvider;

    @Inject
    @Getter(AccessLevel.PROTECTED)
    protected LogoutService logoutService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injector.inject(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setEmptyText(R.string.no_news);
    }

    @Override
    protected void configureList(Activity activity, ListView listView) {
        super.configureList(activity, listView);

        listView.setFastScrollEnabled(true);
        listView.setDividerHeight(0);

        getListAdapter()
                .addHeader(activity.getLayoutInflater()
                        .inflate(R.layout.news_list_item_labels, null));
    }


    @Override
    public void onDestroyView() {
        setListAdapter(null);

        super.onDestroyView();
    }

    @Override
    public Loader<List<DiscountOffer>> onCreateLoader(int id, Bundle args) {
        final List<DiscountOffer> initialItems = items;
        return new ThrowableLoader<List<DiscountOffer>>(getActivity(), items) {

            @Override
            public List<DiscountOffer> loadData() throws Exception {
//                try {
                ArrayList<DiscountOffer> offers = new ArrayList<DiscountOffer>();

                offers.add(new DiscountOffer("Billy's Café", "10% off food and drinks", "08 1234 5678", null));
                offers.add(new DiscountOffer("City Physiotherapy and Sports Injury Clinic", "20% discount on the gap for all visits", "08 8123123123", "http://www.google.com"));
                offers.add(new DiscountOffer("Petstop Gawler", "10% disc. On cash sales excl. fresh pet meat, grooming and point of lay chooks", "(08) 8522 1809", "http://www.petstopgawler.com.au/"));

                if (getActivity() != null) {
                    return offers;
                } else {
                    return Collections.emptyList();
                }

//                } catch (OperationCanceledException e) {
//                    Activity activity = getActivity();
//                    if (activity != null)
//                        activity.finish();
//                    return initialItems;
//                }
            }
        };
    }

    @Override
    protected SingleTypeAdapter<DiscountOffer> createAdapter(List<DiscountOffer> items) {
        return new DiscountListAdapter(getActivity().getLayoutInflater(), items);
    }

    public void onListItemClick(ListView l, View v, int position, long id) {
        DiscountOffer discountOffer = ((DiscountOffer) l.getItemAtPosition(position));

        startActivity(new Intent(getActivity(), DiscountOfferActivity.class).putExtra(DISCOUNT_OFFER_ITEM, discountOffer));
    }

    @Override
    protected int getErrorMessage(Exception exception) {
        return R.string.error_loading_news;
    }

    public class DiscountListAdapter extends AlternatingColorListAdapter<DiscountOffer> {
        /**
         * @param inflater
         * @param items
         * @param selectable
         */
        public DiscountListAdapter(final LayoutInflater inflater, final List<DiscountOffer> items,
                                   final boolean selectable) {
            super(R.layout.news_list_item, inflater, items, selectable);
        }

        /**
         * @param inflater
         * @param items
         */
        public DiscountListAdapter(final LayoutInflater inflater, final List<DiscountOffer> items) {
            super(R.layout.news_list_item, inflater, items);
        }

        @Override
        protected int[] getChildViewIds() {
            return new int[]{R.id.tv_title, R.id.tv_summary,
                    R.id.tv_date};
        }

        @Override
        protected void update(final int position, final DiscountOffer item) {
            super.update(position, item);

            setText(0, item.getBusinessName());
            setText(1, item.getDiscountOffer());
            //setNumber(R.id.tv_date, item.getCreatedAt());
        }
    }

}
