package com.andy6804tw.flirone;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.flir.flironesdk.*;

import java.nio.ByteBuffer;
import java.util.EnumSet;

public class MainActivity extends AppCompatActivity implements Device.Delegate,FrameProcessor.Delegate{

    private Device flirDevice;
    private FrameProcessor frameProcessor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        frameProcessor=new FrameProcessor(this,this, EnumSet.of(RenderedImage.ImageType.BlendedMSXRGBA8888Image));

    }

    @Override
    protected void onResume() {
        super.onResume();
        Device.startDiscovery(this,this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Device.stopDiscovery();
    }

    @Override
    public void onTuningStateChanged(Device.TuningState tuningState) {

    }

    @Override
    public void onAutomaticTuningChanged(boolean b) {

    }

    //與設備連動
    @Override
    public void onDeviceConnected(Device device) {
        flirDevice=device;
        device.startFrameStream(new Device.StreamDelegate() {
            @Override
            public void onFrameReceived(Frame frame) {
                frameProcessor.processFrame(frame);
            }
        });
    }

    @Override
    public void onDeviceDisconnected(Device device) {

    }

    @Override
    public void onFrameProcessed(RenderedImage renderedImage) {
        final Bitmap imageBitmap=Bitmap.createBitmap(renderedImage.width(),renderedImage.height(), Bitmap.Config.ARGB_8888);
        imageBitmap.copyPixelsFromBuffer(ByteBuffer.wrap(renderedImage.pixelData()));;
        final ImageView imageView=(ImageView)findViewById(R.id.imageView) ;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imageView.setImageBitmap(imageBitmap);
            }
        });
    }
}
