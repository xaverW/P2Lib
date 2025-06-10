/*
 * P2Tools Copyright (C) 2023 W. Xaver W.Xaver[at]googlemail.com
 * https://www.p2tools.de
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 */

package de.p2tools.p2lib.mediathek.download;

import de.p2tools.p2lib.tools.log.P2Log;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;

import java.util.concurrent.Semaphore;

/**
 * This singleton class provides the necessary tokens for direct file downloads. It ensures that
 * selected bandwidth limit will not be exceeded for all concurrent direct downloads. Bandwidth
 * throttling based on http://en.wikipedia.org/wiki/Token_bucket
 */
public class MLBandwidthTokenBucket {

    public static final int DEFAULT_BUFFER_SIZE = 4 * 1024; // default byte buffer size
    private final Semaphore bucketSize = new Semaphore(0, false);

    public static final int BANDWIDTH_MAX_BYTE = 10_000_000; // 10 MByte/s, höchste Wert der eingestellt werden kann
    public static final int BANDWIDTH_RUN_FREE = 0; // ist der Wert für Downloads ohne Bremse
    public static final int BANDWIDTH_PAUSE_MAX_BYTE = 50_000; // 50 kByte/s, beim Laden der Filmliste

    private volatile int bucketCapacityByte = BANDWIDTH_RUN_FREE; // capacity BYTE!!
    private MVBandwidthTokenBucketFillerThread fillerThread = null;
    private final IntegerProperty bandwidthValueByte;
    private final BooleanProperty pauseDownloadCapacity;

    public MLBandwidthTokenBucket(IntegerProperty bandwidthValueByte, BooleanProperty pauseDownloadCapacity) {
        this.bandwidthValueByte = bandwidthValueByte; // ist der eingestellte Wert der max. Bandbreite
        this.pauseDownloadCapacity = pauseDownloadCapacity; // true, wenn die Filmliste geladen wird

        setBucketCapacityByte(getBandwidth());
        this.bandwidthValueByte.addListener(l -> {
            P2Log.sysLog("change bucketCapacity: " + getBandwidth() + " bytesPerSecond");
            setBucketCapacityByte(getBandwidth());
        });
        this.pauseDownloadCapacity.addListener(l -> {
            P2Log.sysLog("change bucketCapacity: " + getBandwidth() + " bytesPerSecond");
            setBucketCapacityByte(getBandwidth());
        });
    }

    /**
     * Ensure that bucket filler thread is running. If it running, nothing will happen.
     */
    public synchronized void ensureBucketThreadIsRunning() {
        if (fillerThread == null) {
            fillerThread = new MVBandwidthTokenBucketFillerThread();
            fillerThread.start();
        }
    }

    /**
     * Take number of byte tickets from bucket.
     *
     * @param howMany The number of bytes to acquire.
     */
    public void takeBlocking(final int howMany) {
        // if bucket size equals BANDWIDTH_RUN_FREE then unlimited speed...
        if (getBucketCapacityByte() > BANDWIDTH_RUN_FREE) {
            try {
                bucketSize.acquire(howMany);
            } catch (final Exception ignored) {
            }
        }
    }

    /**
     * Acquire one byte ticket from bucket.
     */
    public void takeBlocking() {
        takeBlocking(1);
    }

    /**
     * Get the capacity of the Token Bucket.
     *
     * @return Maximum number of tokens in the bucket.
     */
    public synchronized int getBucketCapacityByte() {
        return bucketCapacityByte;
    }

    /**
     * Kill the semaphore filling thread.
     */
    private void terminateFillerThread() {
        if (fillerThread != null) {
            fillerThread.interrupt();
            fillerThread = null;
        }
    }

    public synchronized void setBucketCapacityByte(int bucketCapacityByte) {
        this.bucketCapacityByte = bucketCapacityByte;
        terminateFillerThread();
        if (bucketCapacityByte == BANDWIDTH_RUN_FREE) {
            // if we have waiting callers, release them by releasing buckets in the semaphore...
            while (bucketSize.hasQueuedThreads()) {
                bucketSize.release();
            }
            // reset semaphore
            bucketSize.drainPermits();

        } else {
            bucketSize.drainPermits();
            // restart filler thread with new settings...
            ensureBucketThreadIsRunning();
        }
    }

    /**
     * Read bandwidth settings from config.
     *
     * @return The maximum bandwidth in bytes set or zero for unlimited speed.
     */
    private int getBandwidth() {
        if (pauseDownloadCapacity.getValue()) {
            return BANDWIDTH_PAUSE_MAX_BYTE;
        }

        int bytesPerSecond;
        try {
            bytesPerSecond = bandwidthValueByte.get();
        } catch (final Exception ex) {
            P2Log.errorLog(612547803, ex, "reset Bandwidth");
            bytesPerSecond = BANDWIDTH_RUN_FREE;
            bandwidthValueByte.set(BANDWIDTH_RUN_FREE);
        }
        return bytesPerSecond;
    }

    /**
     * Fills the bucket semaphore with available download buckets for speed management.
     */
    private class MVBandwidthTokenBucketFillerThread extends Thread {

        public MVBandwidthTokenBucketFillerThread() {
            setName("MLBandwidthTokenBucket Filler Thread");
        }

        @Override
        public void run() {
            try {
                while (!isInterrupted()) {
                    // run 2times per second, its more regular
                    final int bucketCapacity = getBucketCapacityByte();
                    // for unlimited speed we don't need the thread
                    if (bucketCapacity == BANDWIDTH_RUN_FREE) {
                        break;
                    }

                    final int releaseCount = bucketCapacity / 2 - bucketSize.availablePermits();
                    if (releaseCount > 0) {
                        bucketSize.release(releaseCount);
                    }

                    sleep(500);
                }
            } catch (final Exception ignored) {
            }
        }
    }
}
