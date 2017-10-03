package app.evilhedgehog.twister;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class TalkingTwisterSpinnerActivity extends Activity implements SensorEventListener {
    private HashMap<String, Integer[]> Voices;
    private ToggleButton auto;
    private DecelerateInterpolator dec_inter = new DecelerateInterpolator();
    private int ding;
    private Handler handler = new C01081();
    private Spinner lang_spin;
    private Sensor mAccelerometer;
    private SensorManager mSensorManager;
    private ToggleButton mute;
    private ImageView pointer;
    private int prevrot = 0;
    private String selected_lang;
    private ToggleButton shake;
    private int shake_counter = 3;
    private SoundPool soundPool;
    private HashMap<Integer, Integer> sounds;
    private Button spin;
    private RotateAnimation spin_animation;
    private Timer f0t;
    private int tick;
    private TimerTask tt;
    private Integer[] voices;

    class C01081 extends Handler {

        class C01071 extends TimerTask {
            C01071() {
            }

            public void run() {
                TalkingTwisterSpinnerActivity.this.handler.sendEmptyMessage(-1);
            }
        }

        C01081() {
        }

        public void handleMessage(Message msg) {
            if (msg.getData().getString("time") != null) {
                TalkingTwisterSpinnerActivity.this.f0t = new Timer();
                TalkingTwisterSpinnerActivity.this.tt = new C01071();
                TalkingTwisterSpinnerActivity.this.f0t.schedule(TalkingTwisterSpinnerActivity.this.tt, 0, (long) (Integer.valueOf(msg.getData().getString("time")).intValue() * 1000));
                TalkingTwisterSpinnerActivity.this.disable_all();
            }
            if (msg.what == -1) {
                TalkingTwisterSpinnerActivity.this.animate();
                TalkingTwisterSpinnerActivity.this.pointer.startAnimation(TalkingTwisterSpinnerActivity.this.spin_animation);
            }
            if (msg.what == -2) {
                TalkingTwisterSpinnerActivity.this.auto.setChecked(false);
            }
        }
    }

    class C01112 implements OnCheckedChangeListener {

        class C01091 implements OnClickListener {
            C01091() {
            }

            public void onClick(DialogInterface dialog, int pos) {
                int time = (pos + 1) * 5;
                Message msg = new Message();
                Bundle data = new Bundle();
                data.putString("time", String.valueOf(time));
                msg.setData(data);
                TalkingTwisterSpinnerActivity.this.handler.sendMessage(msg);
            }
        }

        class C01102 implements OnCancelListener {
            C01102() {
            }

            public void onCancel(DialogInterface arg0) {
                TalkingTwisterSpinnerActivity.this.handler.sendEmptyMessage(-2);
            }
        }

        C01112() {
        }

        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                Builder builder = new Builder(buttonView.getContext());
                builder.setTitle(buttonView.getResources().getString(C0106R.string.period));
                builder.setItems(buttonView.getResources().getStringArray(C0106R.array.times), new C01091());
                builder.setOnCancelListener(new C01102());
                builder.create().show();
                return;
            }
            if (TalkingTwisterSpinnerActivity.this.f0t != null) {
                TalkingTwisterSpinnerActivity.this.f0t.cancel();
                if (TalkingTwisterSpinnerActivity.this.tt != null) {
                    TalkingTwisterSpinnerActivity.this.tt.cancel();
                }
                TalkingTwisterSpinnerActivity.this.f0t = null;
                TalkingTwisterSpinnerActivity.this.tt = null;
            }
            TalkingTwisterSpinnerActivity.this.enable_all();
        }
    }

    class C01123 implements OnCheckedChangeListener {
        C01123() {
        }

        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                TalkingTwisterSpinnerActivity.this.register_listener();
                TalkingTwisterSpinnerActivity.this.auto.setEnabled(false);
                return;
            }
            TalkingTwisterSpinnerActivity.this.unregister_listener();
            TalkingTwisterSpinnerActivity.this.auto.setEnabled(true);
        }
    }

    class C01134 implements OnItemSelectedListener {
        C01134() {
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }

        public void onItemSelected(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
            int i;
            if (arg2 == 0) {
                TalkingTwisterSpinnerActivity.this.voices = (Integer[]) TalkingTwisterSpinnerActivity.this.Voices.get("eng");
                TalkingTwisterSpinnerActivity.this.sounds.clear();
                for (i = 0; i < TalkingTwisterSpinnerActivity.this.voices.length; i++) {
                    TalkingTwisterSpinnerActivity.this.sounds.put(Integer.valueOf(i), Integer.valueOf(TalkingTwisterSpinnerActivity.this.soundPool.load(arg1.getContext(), TalkingTwisterSpinnerActivity.this.voices[i].intValue(), 1)));
                }
                return;
            }
            TalkingTwisterSpinnerActivity.this.voices = (Integer[]) TalkingTwisterSpinnerActivity.this.Voices.get("hun");
            TalkingTwisterSpinnerActivity.this.sounds.clear();
            for (i = 0; i < TalkingTwisterSpinnerActivity.this.voices.length; i++) {
                TalkingTwisterSpinnerActivity.this.sounds.put(Integer.valueOf(i), Integer.valueOf(TalkingTwisterSpinnerActivity.this.soundPool.load(arg1.getContext(), TalkingTwisterSpinnerActivity.this.voices[i].intValue(), 1)));
            }
        }
    }

    class C01145 implements View.OnClickListener {
        C01145() {
        }

        public void onClick(View v) {
            TalkingTwisterSpinnerActivity.this.disable_all();
            TalkingTwisterSpinnerActivity.this.animate();
            TalkingTwisterSpinnerActivity.this.pointer.startAnimation(TalkingTwisterSpinnerActivity.this.spin_animation);
        }
    }

    class C01156 implements AnimationListener {
        C01156() {
        }

        public void onAnimationEnd(Animation animation) {
            if (!TalkingTwisterSpinnerActivity.this.mute.isChecked()) {
                TalkingTwisterSpinnerActivity.this.soundPool.play(((Integer) TalkingTwisterSpinnerActivity.this.sounds.get(Integer.valueOf(((TalkingTwisterSpinnerActivity.this.prevrot / 90) * 4) + ((int) (Math.floor((double) (TalkingTwisterSpinnerActivity.this.prevrot % 90)) / 22.5d))))).intValue(), 1.0f, 1.0f, 1, 0, 1.0f);
            }
            TalkingTwisterSpinnerActivity.this.enable_all();
            TalkingTwisterSpinnerActivity.this.ShowToast();
        }

        public void onAnimationRepeat(Animation animation) {
        }

        public void onAnimationStart(Animation animation) {
        }
    }

    protected void onDestroy() {
        if (this.f0t != null) {
            this.f0t.cancel();
            if (this.tt != null) {
                this.tt.cancel();
            }
            this.f0t = null;
            this.tt = null;
        }
        this.mSensorManager.unregisterListener(this);
        super.onDestroy();
    }

    protected void onPause() {
        if (this.f0t != null) {
            this.f0t.cancel();
            if (this.tt != null) {
                this.tt.cancel();
            }
            this.f0t = null;
            this.tt = null;
        }
        this.mSensorManager.unregisterListener(this);
        super.onPause();
    }

    protected void onResume() {
        if (this.shake != null && this.shake.isChecked()) {
            register_listener();
        }
        super.onResume();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(1);
        requestWindowFeature(1);
        getWindow().addFlags(4224);
        getWindow().setFlags(1024, 1024);
        setContentView(C0106R.layout.main);
        ((AdView) findViewById(C0106R.id.adView)).loadAd(new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).addTestDevice("D072C2B4C472F8837D55703BA1A77929").addTestDevice("1754ECE553428C26245EBA2F64DE34C2").build());
        setVolumeControlStream(3);
        this.soundPool = new SoundPool(2, 3, 100);
        this.tick = this.soundPool.load(this, C0106R.raw.tick, 1);
        this.ding = this.soundPool.load(this, C0106R.raw.ding, 1);
        this.selected_lang = getString(C0106R.string.lang);
        this.mute = (ToggleButton) findViewById(C0106R.id.mute);
        this.shake = (ToggleButton) findViewById(C0106R.id.ShakeToggle);
        this.pointer = (ImageView) findViewById(C0106R.id.pointer_view);
        this.auto = (ToggleButton) findViewById(C0106R.id.AutoToggle);
        this.lang_spin = (Spinner) findViewById(C0106R.id.Spinner01);
        this.mSensorManager = (SensorManager) getSystemService("sensor");
        this.mAccelerometer = this.mSensorManager.getDefaultSensor(1);
        this.auto.setOnCheckedChangeListener(new C01112());
        this.shake.setOnCheckedChangeListener(new C01123());
        this.lang_spin.setOnItemSelectedListener(new C01134());
        this.spin = (Button) findViewById(C0106R.id.button1);
        this.spin.setOnClickListener(new C01145());
        InitializeVoices();
    }

    private void register_listener() {
        this.mSensorManager.registerListener(this, this.mAccelerometer, 3);
    }

    private void unregister_listener() {
        this.mSensorManager.unregisterListener(this);
    }

    private void disable_all() {
        this.mute.setEnabled(false);
        this.shake.setEnabled(false);
        this.lang_spin.setEnabled(false);
        this.spin.setClickable(false);
    }

    private void enable_all() {
        if (this.f0t == null) {
            this.mute.setEnabled(true);
            this.shake.setEnabled(true);
            this.lang_spin.setEnabled(true);
            this.spin.setClickable(true);
        }
    }

    private void InitializeVoices() {
        this.sounds = new HashMap(16);
        Integer[] eng = new Integer[]{Integer.valueOf(C0106R.raw.right_h_g), Integer.valueOf(C0106R.raw.right_h_b), Integer.valueOf(C0106R.raw.right_h_y), Integer.valueOf(C0106R.raw.right_h_r), Integer.valueOf(C0106R.raw.right_f_g), Integer.valueOf(C0106R.raw.right_f_b), Integer.valueOf(C0106R.raw.right_f_y), Integer.valueOf(C0106R.raw.right_f_r), Integer.valueOf(C0106R.raw.left_h_g), Integer.valueOf(C0106R.raw.left_h_b), Integer.valueOf(C0106R.raw.left_h_y), Integer.valueOf(C0106R.raw.left_h_r), Integer.valueOf(C0106R.raw.left_f_g), Integer.valueOf(C0106R.raw.left_f_b), Integer.valueOf(C0106R.raw.left_f_y), Integer.valueOf(C0106R.raw.left_f_r)};
        Integer[] hun = new Integer[]{Integer.valueOf(C0106R.raw.right_h_g_hun), Integer.valueOf(C0106R.raw.right_h_b_hun), Integer.valueOf(C0106R.raw.right_h_y_hun), Integer.valueOf(C0106R.raw.right_h_r_hun), Integer.valueOf(C0106R.raw.right_f_g_hun), Integer.valueOf(C0106R.raw.right_f_b_hun), Integer.valueOf(C0106R.raw.right_f_y_hun), Integer.valueOf(C0106R.raw.right_f_r_hun), Integer.valueOf(C0106R.raw.left_h_g_hun), Integer.valueOf(C0106R.raw.left_h_b_hun), Integer.valueOf(C0106R.raw.left_h_y_hun), Integer.valueOf(C0106R.raw.left_h_r_hun), Integer.valueOf(C0106R.raw.left_f_g_hun), Integer.valueOf(C0106R.raw.left_f_b_hun), Integer.valueOf(C0106R.raw.left_f_y_hun), Integer.valueOf(C0106R.raw.left_f_r_hun)};
        this.Voices = new HashMap();
        this.Voices.put("eng", eng);
        this.Voices.put("hun", hun);
        this.voices = (Integer[]) this.Voices.get(this.selected_lang);
        for (int i = 0; i < this.voices.length; i++) {
            this.sounds.put(Integer.valueOf(i), Integer.valueOf(this.soundPool.load(this, this.voices[i].intValue(), 1)));
        }
        if (this.selected_lang.equals("hun")) {
            this.lang_spin.setSelection(1);
        } else {
            this.lang_spin.setSelection(0);
        }
    }

    private void ShowToast() {
        String[] texts = getApplicationContext().getResources().getStringArray(C0106R.array.ToastText);
        Toast.makeText(getApplicationContext(), texts[this.prevrot / 90] + texts[((int) (Math.floor((double) (this.prevrot % 90)) / 22.5d)) + 4], 1).show();
    }

    private void animate() {
        int r = (int) (Math.random() * 360.0d);
        this.spin_animation = new RotateAnimation((float) this.prevrot, (float) (r + 1800), 0, (float) (((this.spin.getMeasuredWidth() / 2) - this.pointer.getLeft()) + this.spin.getLeft()), 0, (float) (((this.spin.getMeasuredHeight() / 2) + this.spin.getTop()) - this.pointer.getTop()));
        this.spin_animation.setFillAfter(true);
        this.spin_animation.setInterpolator(this.dec_inter);
        this.spin_animation.setDuration(3500);
        this.spin_animation.setAnimationListener(new C01156());
        this.prevrot = r % 360;
        if (!this.mute.isChecked()) {
            this.soundPool.play(this.tick, 1.0f, 1.0f, 1, 0, 1.0f);
        }
    }

    private void play_ding() {
        this.soundPool.play(this.ding, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void onSensorChanged(SensorEvent event) {
        if (((double) Math.abs(event.values[0])) <= 10.787315177917481d && ((double) Math.abs(event.values[1])) <= 10.787315177917481d && ((double) Math.abs(event.values[2])) <= 13.729310226440429d) {
            return;
        }
        if (this.shake_counter > 0) {
            this.shake_counter--;
            return;
        }
        play_ding();
        disable_all();
        animate();
        this.pointer.startAnimation(this.spin_animation);
        this.shake_counter = 3;
    }
}
