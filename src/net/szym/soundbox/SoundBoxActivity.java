// Copyright (c) 2011 Szymon Jakubczak. All rights reserved.
// Use of this source code is governed by a license that can be found in
// the LICENSE file.
package net.szym.soundbox;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.ToggleButton;

// this is just for testing SoundBox
// you don't actually need any controls
// simply load the clips one by one box.load(filename)
// keep track of the ids it gives you for later reference
// then box.play(id), box.pause(id), box.setVolume(id)...
public class SoundBoxActivity extends Activity {
  SoundBox box = new SoundBox();

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    String[] files = {
        "/sdcard/test1.wav",
        "/sdcard/test2.wav",
        "/sdcard/test3.wav",
        "/sdcard/test4.wav",
    };

    // create one table row per clip
    // each row has toggle button (play/pause), seekbar (volume), and seekbar
    // (position)
    LinearLayout layout = (LinearLayout) findViewById(R.id.layout);
    for (int i = 0; i < files.length; ++i) {
      int id = box.load(files[i]);
      LinearLayout row = new LinearLayout(this);
      row.setOrientation(LinearLayout.HORIZONTAL);
      // These blocks are not essential, but they help limit the scope.
      // This way I don't need to worry whether it's the first time I declare
      // SeekBar bar, or re-use the variable.
      // With blocks, each declaration is independent and goes away when the
      // block is closed.
      {
        ToggleButton button = new ToggleButton(this);
        button.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT, 1.0f));
        button.setTag(id);
        button.setOnCheckedChangeListener(new OnCheckedChangeListener() {
          public void onCheckedChanged(CompoundButton buttonView,
              boolean isChecked) {
            int id = (Integer) buttonView.getTag();
            if (isChecked)
              box.play(id);
            else
              box.pause(id);
          }
        });
        row.addView(button);
      }
      {
        SeekBar bar = new SeekBar(this);
        bar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT, 1.0f));
        bar.setMax(100);
        bar.setProgress(100);
        bar.setTag(id);
        bar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
          public void onStopTrackingTouch(SeekBar seekBar) {
          }

          public void onStartTrackingTouch(SeekBar seekBar) {
          }

          public void onProgressChanged(SeekBar seekBar, int progress,
              boolean fromUser) {
            int id = (Integer) seekBar.getTag();
            float vol = (float) progress / seekBar.getMax();
            box.setVolume(id, vol, vol);
          }
        });
        row.addView(bar);
      }
      {
        SeekBar bar = new SeekBar(this);
        bar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT, 1.0f));
        bar.setMax(100);
        bar.setTag(id);
        bar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
          public void onStopTrackingTouch(SeekBar seekBar) {
            int id = (Integer) seekBar.getTag();
            // this would probably be more precise if we used precise frame
            // count rather than floating-point
            // but we would have to wait until the clip is loaded before we can
            // do bar.setMax(...)
            float pos = (float) seekBar.getProgress() / seekBar.getMax();
            box.seek(id, (int) (pos * box.getLength(id)));
          }

          public void onStartTrackingTouch(SeekBar seekBar) {
          }

          public void onProgressChanged(SeekBar seekBar, int progress,
              boolean fromUser) {
          }
        });
        row.addView(bar);
      }

      layout.addView(row);
    }
  }
}