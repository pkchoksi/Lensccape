package com.pkchoksi.pkc.lenscape;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseRole;
import com.parse.ParseUser;

/**
 * Created by pkcho on 2/13/2016.
 */
public class CustomGroupsAdapter extends ParseQueryAdapter<ParseObject> {
    public CustomGroupsAdapter(Context context, final ParseUser user) {
        super(context, new ParseQueryAdapter.QueryFactory<ParseObject>(){
            public ParseQuery create() {

                ParseQuery query = ParseRole.getQuery();
                query.whereEqualTo("Admin",user);
                return query;
            }

        });
    }
    public View getItemView(ParseObject object, View v, ViewGroup parent) {
        if (v == null) {
            v = View.inflate(getContext(), R.layout.user_list, null);
        }

        super.getItemView(object, v, parent);
        TextView Displaygroupname = (TextView) v.findViewById(R.id.userListItem);
        if (object != null) {
            Displaygroupname.setText(object.get("name").toString());
        }else {
            Toast.makeText(getContext(),"you must be enrolled in a group",Toast.LENGTH_SHORT).show();
        }
        return v;
    }
}
