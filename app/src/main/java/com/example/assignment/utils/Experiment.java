/*
package com.example.assignment.utils;

import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import javax.xml.transform.Source;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class Experiment {

    private static final String TAG = "TAG";

    @RequiresApi(api = Build.VERSION_CODES.N)
    private Single<Boolean> getLocalSync(boolean isNewContactAdded) {
        return Single.zip(this.readNativeContacts(isNewContactAdded), this.getAppContacts(Source.LOCAL.name(), Source.REMOTE.name()), (nativeContacts, appContacts) -> {
            List<Participant> addList = new ArrayList(nativeContacts);
            addList.removeAll(appContacts);
            Stream.of(addList).forEach((participant) -> {
                participant.setSyncState(1);
            });
            List<Participant> commonList = new ArrayList(nativeContacts);
            commonList.removeAll(addList);
            if (commonList.size() > 0) {
                ParticipantRepository.getInstance().updateNativeInfo(commonList);
            }

            addList.sort((o1, o2) -> {
                return o1.getMDisplayName().compareTo(o2.getMDisplayName());
            });
            ParticipantRepository.getInstance().insertOrUpdate(addList);
            FileLogUtils.v(TAG, "==>> addList size: " + addList.size() + " commonList size: " + commonList.size());
            if (!nativeContacts.isEmpty()) {
                Set<String> lookupKeySet = new HashSet();
                Stream.of(nativeContacts).forEach((participant) -> {
                    String lookupKey = participant.getMContactLookupKey();
                    if (!TextUtils.isEmpty(lookupKey)) {
                        lookupKeySet.add(lookupKey);
                    }

                });

                this.mContactProviderOperationsManager.readNativeContactDetailsInBatches(new ArrayList(lookupKeySet), (HashMap)null);
            }

            if (!this.readNativeInProgress) {
                this.mContactProviderOperationsManager.checkDeletedNativeContacts();
            }

            return true;
        }).subscribeOn(Schedulers.io());
    }

}
*/
