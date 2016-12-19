package com.example.shubham.taskh.utility;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Utility class for fragment calling
 * <p/>
 * Created by shubham on 15/12/16.
 */
public class FragmentCall {

    /**
     * Adds the fragment to the fragment_container.
     * Adds the fragment to the back stack.
     * also allows options for add to back stack and replace
     *
     * @param iFragment        this fragment is inflated to the layout.
     * @param iFragmentManager instantiates the Fragment Manager.
     */
    public static void inflateFragment(Fragment iFragment, FragmentManager iFragmentManager,
                                       int iFragmentContainerId, Bundle iBundle,
                                       boolean isAddToBackStack, boolean isReplace) {
        try {
            if (iFragment != null && iFragmentManager != null) {
                FragmentTransaction transaction = iFragmentManager.beginTransaction();
                if (iBundle != null)
                    iFragment.setArguments(iBundle);
                if (isReplace)
                    transaction.replace(iFragmentContainerId, iFragment);
                else
                    transaction.add(iFragmentContainerId, iFragment);

                if (isAddToBackStack)
                    transaction.addToBackStack(iFragment.getClass().getSimpleName());

                transaction.commit();
            }
        } catch (NullPointerException | IllegalStateException e) {
            e.printStackTrace();
        }
    }
}
