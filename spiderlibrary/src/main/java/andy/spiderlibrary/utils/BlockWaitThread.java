package andy.spiderlibrary.utils;

import java.util.ArrayDeque;

/**
 * Created by andyli on 2015/11/21.
 */
public abstract class BlockWaitThread<T> extends Thread {

    private ArrayDeque<T> mTaskList;
    private boolean mDone;
    private boolean mIsPause;
    private static final int mThreadPriority = android.os.Process.THREAD_PRIORITY_BACKGROUND;
    private static final int DEFAULT_QUEUE_SIZE = 14;

    protected BlockWaitThread(String name) {
        this(name, mThreadPriority);
    }

    protected BlockWaitThread(String name, int threadPriority) {
        this(name, threadPriority, DEFAULT_QUEUE_SIZE);
    }

    protected BlockWaitThread(String name, int threadPriority, int queueSize) {
        super(name);

        mTaskList = new SizeLimitLinkedList<T>(queueSize);
    }

    public void stopWork() {
        mDone = true;
        synchronized (mTaskList) {
            mTaskList.clear();
        }
        wakeUp();
    }

    public void pauseWork() {
        mIsPause = true;
        wakeUp();
    }

    public void resumeWork() {
        mIsPause = false;
        wakeUp();
    }

    public void addTask(T item) {
        if (mDone) {
            return;
        }
        synchronized (mTaskList) {
            if (!mTaskList.contains(item)) {
                mTaskList.addFirst(item);
            }
        }
        wakeUp();
    }

    public void clearTask() {
        synchronized (mTaskList) {
            mTaskList.clear();
        }
        wakeUp();
    }

    public void wakeUp() {
        synchronized (BlockWaitThread.this) {
            BlockWaitThread.this.notify();
        }
    }

    public int getTaskSize(){
        synchronized (mTaskList) {
            return mTaskList.size();
        }
    }

    public boolean containsList(T t) {
        synchronized (mTaskList) {
            return mTaskList.contains(t);
        }
    }

    @Override
    public void run() {
        android.os.Process.setThreadPriority(mThreadPriority);
        while (!mDone) {
            synchronized (BlockWaitThread.this) {
                if (mTaskList == null || mTaskList.size() == 0 || mIsPause) {
                    try {
                        BlockWaitThread.this.wait();
                    } catch (InterruptedException ex) {
                        continue;
                    }
                    continue;
                }
            }

            T item = null;
            synchronized (mTaskList) {
                if (mTaskList.isEmpty()) {
                    continue;
                }
                item = mTaskList.removeFirst();
            }
            if (mIsPause) {
                continue;
            }

            handleItem(item);
        }
    }

    abstract protected void handleItem(T item);

    private class SizeLimitLinkedList<S> extends ArrayDeque<S> {
        private static final long serialVersionUID = 1L;
        private int queueSize;

        public SizeLimitLinkedList(int size) {
            super(size);
            queueSize = size;
        }

        @Override
        public void addFirst(S object) {
            if (size() + 1 > queueSize) {
                super.removeLast();
            }
            super.addFirst(object);
        }


    }
}