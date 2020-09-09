package com.sunward.test

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.util.MutableBoolean
import androidx.appcompat.app.AppCompatActivity
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng
import com.baidu.mapapi.model.LatLngBounds
import com.baidu.mapapi.model.inner.Point
import kotlinx.android.synthetic.main.activity_main.*
import mapsdkvi.com.gdi.bgl.android.java.EnvDrawText.bmp
import java.io.IOException
import java.io.InputStream
import java.nio.BufferUnderflowException
import java.nio.ByteBuffer


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bmapView.map.mapType = BaiduMap.MAP_TYPE_NONE
        // 获取/assets目录下的所有文件
        val images = assets.list("output/3/0");
        Log.e("ss","ss")
//        bmapView.map.mapType = BaiduMap.MAP_TYPE_NONE
        /**
         * 定义瓦片图的离线Provider，并实现相关接口
         * MAX_LEVEL、MIN_LEVEL 表示地图显示瓦片图的最大、最小级别
         * Tile 对象表示地图每个x、y、z状态下的瓦片对象
         */
        /**
         * 定义瓦片图的离线Provider，并实现相关接口
         * MAX_LEVEL、MIN_LEVEL 表示地图显示瓦片图的最大、最小级别
         * Tile 对象表示地图每个x、y、z状态下的瓦片对象
         */
        val tileProvider = object : FileTileProvider() {
            override fun getTile(x: Int, y: Int, z: Int): Tile {
                // 根据地图某一状态下x、y、z加载指定的瓦片图
                val filedir = "output/3/0/0.jpg"
                //将瓦片图资源解析为Bitmap
                val bm: Bitmap = getBitmapFromAsset(this@MainActivity, filedir) ?: return Tile(
                    256, 256, ByteArray(
                        256 * 256
                    )
                )
                // 通过瓦片图bitmap构造Tile示例
                val offlineTile = Tile(bm.width, bm.height, toRawData(bm))
                bm.recycle()
                return offlineTile
            }

            override fun getMaxDisLevel(): Int {
                return 21
            }

            override fun getMinDisLevel(): Int {
                return 3
            }
        }

//构造TileOverlayOptions对象并设置FileTileProvider等属性

//构造TileOverlayOptions对象并设置FileTileProvider等属性
        val options = TileOverlayOptions()
// 构造显示瓦片图范围，当前为世界范围
// 构造显示瓦片图范围，当前为世界范围
        val northeast = LatLng(28.259582520,113.189392090)
        val southwest = LatLng(28.245849609,113.171539307)
//        val northeast = LatLng(80.0, 180.0)
//        val southwest = LatLng(-80.0, -180.0)
        options.tileProvider(tileProvider)
                .setPositionFromBounds(
                    LatLngBounds.Builder().include(northeast).include(southwest).build()
                )
//向地图添加离线瓦片图对象
//向地图添加离线瓦片图对象

        val tileOverlay = bmapView.map.addTileLayer(options)
        Log.e("ss", "ss")
        bmapView.map.setMapStatus(MapStatusUpdateFactory.newLatLngBounds(LatLngBounds.Builder().include(northeast).include(southwest).build()));
    }

    fun toRawData(bm: Bitmap): ByteArray? {
        val size = bm.rowBytes * bm.height
        val b: ByteBuffer = ByteBuffer.allocate(size)

        bm.copyPixelsToBuffer(b)

        val bytes = ByteArray(size)

        try {
            return b.array()
//            b.get(bytes, 0, bytes.size)
        } catch (e: BufferUnderflowException) {
            e.printStackTrace()
            return null
        }
        return bytes
    }

    fun getBitmapFromAsset(context: Context, filePath: String?): Bitmap? {
        val istr: InputStream
        var bitmap: Bitmap? = null
        try {
            istr = assets.open(filePath!!)
            bitmap = BitmapFactory.decodeStream(istr)
        } catch (e: IOException) {
            // handle exception
        }
        return bitmap
    }

}