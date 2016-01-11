package io.github.naotou.testleancloud;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.DeleteCallback;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.btn_1) Button mButton;
    String tablePost = "Post";
    private AVObject mPost;

    @OnClick(R.id.btn_1)
    void create() {
        Observable.create(new rx.Observable.OnSubscribe<Void>() {
            @Override
            public void call(Subscriber<? super Void> subscriber) {

                mPost = new AVObject(tablePost);
                mPost.put("username", "daigou");
                mPost.put("userage", 29);
                try {
                    mPost.save();
                    subscriber.onCompleted();
                } catch (AVException e) {
                    subscriber.onError(e);
                    e.printStackTrace();
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnSubscribe(new Action0() {
            @Override
            public void call() {
                mButton.setText("loading");
            }
        }).subscribe(new Subscriber<Void>() {
            @Override
            public void onCompleted() {
                mButton.setText("success");
            }

            @Override
            public void onError(Throwable e) {
                mButton.setText("error");
            }

            @Override
            public void onNext(Void aVoid) {

            }
        });
    }

    @OnClick(R.id.btn_2)
    void update() {
        Observable.create(new Observable.OnSubscribe<Void>() {
            @Override
            public void call(Subscriber<? super Void> subscriber) {
                AVQuery<AVObject> avQuery = new AVQuery<>(getTablePost());
                try {
                    AVObject object = avQuery.get(mPost.getObjectId());
                    object.put("userage", 30);
                    object.save();
                    subscriber.onCompleted();
                } catch (AVException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick(R.id.btn_3)
    void read() {
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {

                AVObject post = null;
                AVQuery<AVObject> query = new AVQuery<>(getTablePost());
                try {
                    post = query.get(mPost.getObjectId());
                    String username = post.getString("username");
                    int userage = post.getInt("userage");
                    subscriber.onNext(username);
                    subscriber.onNext(String.valueOf(userage));
                    subscriber.onCompleted();
                } catch (AVException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @NonNull
    private String getTablePost() {return "Post";}

    @OnClick(R.id.btn_4)
    void delete() {
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                AVQuery<AVObject> avQuery = new AVQuery<>(getTablePost());
                avQuery.deleteAllInBackground(new DeleteCallback() {
                    @Override
                    public void done(AVException e) {
                        subscriber.onNext(e.getMessage());
                        subscriber.onCompleted();
                    }
                });
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick(R.id.btn_5)
    void show() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
    }
}
