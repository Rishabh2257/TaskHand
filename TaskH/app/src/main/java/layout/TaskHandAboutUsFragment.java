package layout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.shubham.taskh.R;
import com.squareup.picasso.Picasso;

/**
 * About us Fragment for app description of its features
 */
public class TaskHandAboutUsFragment extends Fragment {

    private ImageView mTaskHandImage;
    private View mView;

    public TaskHandAboutUsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_task_hand_about_us, container, false);
        mTaskHandImage = (ImageView) mView.findViewById(R.id.task_hand_background_about_us);
        Picasso.with(getActivity()).load(R.drawable.img_logo_background).into(mTaskHandImage);

        return mView;
    }

}
