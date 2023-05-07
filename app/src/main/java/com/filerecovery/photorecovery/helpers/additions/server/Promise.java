package com.filerecovery.photorecovery.helpers.additions.server;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Promise {
    private static final String TAG = "Promise";
    private Promise child;
    private Handler handler = new Handler(Looper.getMainLooper());
    private boolean isRejected;
    private boolean isResolved;
    private OnErrorListener onErrorListener;
    private OnSuccessListener onSuccessListener;
    private Object rejectedObject;
    private Object resolvedObject;
    private Object tag;

    public interface OnErrorListener {
        void onError(Object obj);
    }

    public interface OnSuccessListener {
        Object onSuccess(Object obj);
    }

    public static Promise chain(Object obj) {
        Promise promise = new Promise();
        promise.resolve(obj);
        return promise;
    }

    public static Promise all(final Promise... promiseArr) {
        final Promise promise = new Promise();
        final Object[] objArr = new Object[(promiseArr != null ? promiseArr.length : 0)];
        if (promiseArr == null || promiseArr.length <= 0) {
            promise.resolve(objArr);
            return promise;
        }
        if (promiseArr == null || promiseArr.length <= 0) {
            promise.resolve(objArr);
        } else {
            new Runnable() {
                int completedCount = 0;

                public void run() {
                    int i = 0;
                    while (true) {
                        if (i < promiseArr.length) {
                            Promise promise = promiseArr[i];
                            promise.setTag(Integer.valueOf(i));
                            promise.then(new OnSuccessListener() {
                                public final Object onSuccess(Object obj) {
                                    objArr[((Integer) promise.getTag()).intValue()] = obj;
                                    completedCount++;
                                    if (obj != null) {
                                        promise.reject(obj);
                                    } else if (completedCount == promiseArr.length) {
                                        promise.resolve(objArr);
                                    }
                                    return obj;

                                }
                            }).error(new OnErrorListener() {
                                public final void onError(Object obj) {
                                    completedCount++;
                                    if (obj != null) {
                                        promise.reject(obj);
                                    } else if (completedCount == promiseArr.length) {
                                        promise.resolve(objArr);
                                    }
                                }
                            });
                            i++;
                        } else {
                            return;
                        }
                    }
                }
            }.run();
        }
        return promise;
    }

    public static Promise series(final List<?> list, final OnSuccessListener onSuccessListener2) {
        final Promise promise = new Promise();
        final ArrayList arrayList = new ArrayList(list != null ? list.size() : 0);
        if (list == null || onSuccessListener2 == null || list.size() <= 0) {
            promise.resolve(arrayList);
            return promise;
        }
        new Runnable() {
            int completedCount = 0;
            int index = -1;

            public void run() {
                this.index++;
                if (this.index < list.size()) {
                    int i = this.index;
                    handleSuccess(i, list.get(i));
                    return;
                }
                promise.resolve(arrayList);
            }

            private void handleSuccess(int i, Object obj) {
                Object onSuccess = onSuccessListener2.onSuccess(obj);
                arrayList.add(i, onSuccess);
                if (onSuccess instanceof Promise) {
                    Promise promise = (Promise) onSuccess;
                    promise.setTag(Integer.valueOf(i));
                    promise.then(new OnSuccessListener() {
                        public final Object onSuccess(Object obj) {
                            arrayList.set(((Integer) promise.getTag()).intValue(), obj);
                            if (!lambda$handleSuccess$1$Promise$2((Object) null)) {
                                run();
                            }
                            return obj;
                        }
                    }).error(new OnErrorListener() {
                        public final void onError(Object obj) {
                            lambda$handleSuccess$1$Promise$2(obj);
                        }
                    });
                } else if (!lambda$handleSuccess$1$Promise$2((Object) null)) {
                    run();
                }
            }

            public  Object lambda$handleSuccess$0$Promise$2(ArrayList arrayList, Promise promise, Object obj) {
                arrayList.set(((Integer) promise.getTag()).intValue(), obj);
                if (!lambda$handleSuccess$1$Promise$2((Object) null)) {
                    run();
                }
                return obj;
            }


            public boolean lambda$handleSuccess$1$Promise$2(Object obj) {
                this.completedCount++;
                if (obj != null) {
                    promise.reject(obj);
                    return false;
                } else if (this.completedCount != list.size()) {
                    return false;
                } else {
                    promise.resolve(arrayList);
                    return true;
                }
            }
        }.run();
        return promise;
    }

    public static Promise parallel(final List<?> list, final OnSuccessListener onSuccessListener2) {
        final Promise promise = new Promise();
        final ArrayList arrayList = new ArrayList(list != null ? list.size() : 0);
        if (list == null || onSuccessListener2 == null || list.size() <= 0) {
            Log.e(TAG, "Arguments should not be NULL!");
            promise.resolve(arrayList);
            return promise;
        }
        new Runnable() {
            int completedCount = 0;

            public void run() {
                if (list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        handleSuccess(i, list.get(i));
                    }
                    return;
                }
                promise.resolve(arrayList);
            }

            private void handleSuccess(int i, Object obj) {
                Object onSuccess = onSuccessListener2.onSuccess(obj);
                arrayList.add(i, onSuccess);
                if (onSuccess instanceof Promise) {
                    Promise promise = (Promise) onSuccess;
                    promise.setTag(Integer.valueOf(i));
                    promise.then(new OnSuccessListener() {
                        public final Object onSuccess(Object obj) {
                            arrayList.set(((Integer) promise.getTag()).intValue(), obj);
                            lambda$handleSuccess$1$Promise$3((Object) null);
                            return obj;
                        }
                    }).error(new OnErrorListener() {
                        public final void onError(Object obj) {
                            lambda$handleSuccess$1$Promise$3(obj);
                        }
                    });
                    return;
                }
                lambda$handleSuccess$1$Promise$3((Object) null);
            }


            public void lambda$handleSuccess$1$Promise$3(Object obj) {
                this.completedCount++;
                if (obj != null) {
                    promise.reject(obj);
                } else if (this.completedCount == list.size()) {
                    promise.resolve(arrayList);
                }
            }
        }.run();
        return promise;
    }

    public Promise parallelWithLimit(List<?> list, int i, OnSuccessListener onSuccessListener2) {
        Promise promise = new Promise();
        if (list == null || onSuccessListener2 == null || list.size() <= 0 || i <= 0) {
            return null;
        }
        series(list, new OnSuccessListener() {

            public final Object onSuccess(Object obj) {
                return Promise.lambda$parallelWithLimit$2(new int[1], new int[1], i, new ArrayList(), list, onSuccessListener2, obj);
            }
        }).then(new OnSuccessListener() {
            public final Object onSuccess(Object obj) {
                return Promise.this.resolve(obj);
            }
        });
        return promise;
    }

    static Object lambda$parallelWithLimit$2(int[] iArr, int[] iArr2, int i, ArrayList arrayList, List list, OnSuccessListener onSuccessListener2, Object obj) {
        Promise promise = new Promise();
        iArr[0] = iArr[0] + 1;
        iArr2[0] = iArr2[0] + 1;
        if (iArr[0] < i) {
            arrayList.add(obj);
            if (iArr2[0] == list.size()) {
                parallel(arrayList, onSuccessListener2).then(new OnSuccessListener() {
                    public final Object onSuccess(Object obj) {
                        promise.resolve(obj);
                        iArr[0] = 0;
                        arrayList.clear();
                        return obj;
                    }
                });
            } else {
                promise.resolve(true);
            }
        } else if (iArr[0] == i) {
            arrayList.add(obj);
            parallel(arrayList, onSuccessListener2).then(new OnSuccessListener() {

                public final Object onSuccess(Object obj) {
                    promise.resolve(obj);
                    iArr[0] = 0;
                    arrayList.clear();
                    return obj;
                }
            });
        }
        return promise;
    }

    Object lambda$null$0(Promise promise, int[] iArr, ArrayList arrayList, Object obj) {
        promise.resolve(obj);
        iArr[0] = 0;
        arrayList.clear();
        return obj;
    }

    Object lambda$null$1(Promise promise, int[] iArr, ArrayList arrayList, Object obj) {
        promise.resolve(obj);
        iArr[0] = 0;
        arrayList.clear();
        return obj;
    }

    public Object resolve(Object obj) {
        if (!this.isResolved) {
            this.isResolved = true;
            this.resolvedObject = obj;
            if (this.onSuccessListener != null) {
                Handler handler2 = this.handler;
                if (handler2 != null) {
                    handler2.post(new Runnable() {
                        public final void run() {
                            Promise.this.lambda$resolve$4$Promise();
                        }
                    });
                } else {
                    new Thread(new Runnable() {
                        public final void run() {
                            Promise.this.lambda$resolve$5$Promise();
                        }
                    }).start();
                }
            }
        } else {
            Log.e(TAG, "The promise already resolved, you can not resolve same promise multiple time!");
        }
        return obj;
    }

    public  void lambda$resolve$4$Promise() {
        handleSuccess(this.child, this.resolvedObject);
    }

    public  void lambda$resolve$5$Promise() {
        handleSuccess(this.child, this.resolvedObject);
    }

    public Object reject(Object obj) {
        if (!this.isRejected) {
            this.isRejected = true;
            this.rejectedObject = obj;
            if (this.onErrorListener != null) {
                Handler handler2 = this.handler;
                if (handler2 != null) {
                    handler2.post(new Runnable() {
                        public final void run() {
                            Promise.this.lambda$reject$6$Promise();
                        }
                    });
                } else {
                    new Thread(new Runnable() {
                        public final void run() {
                            Promise.this.lambda$reject$7$Promise();
                        }
                    }).start();
                }
            } else {
                Promise promise = this.child;
                if (promise != null) {
                    promise.reject(obj);
                }
            }
        } else {
            Log.e(TAG, "The promise already rejected, you can not reject same promise multiple time!");
        }
        return obj;
    }

    public  void lambda$reject$6$Promise() {
        handleError(this.rejectedObject);
    }

    public  void lambda$reject$7$Promise() {
        handleError(this.rejectedObject);
    }

    public Promise then(OnSuccessListener onSuccessListener2) {
        this.onSuccessListener = onSuccessListener2;
        this.child = new Promise();
        if (this.isResolved) {
            Handler handler2 = this.handler;
            if (handler2 != null) {
                handler2.post(new Runnable() {
                    public final void run() {
                        Promise.this.lambda$then$8$Promise();
                    }
                });
            } else {
                new Thread(new Runnable() {
                    public final void run() {
                        Promise.this.lambda$then$9$Promise();
                    }
                }).start();
            }
        }
        return this.child;
    }

    public  void lambda$then$8$Promise() {
        handleSuccess(this.child, this.resolvedObject);
    }

    public  void lambda$then$9$Promise() {
        handleSuccess(this.child, this.resolvedObject);
    }

    public void error(OnErrorListener onErrorListener2) {
        this.onErrorListener = onErrorListener2;
        if (this.isRejected) {
            Handler handler2 = this.handler;
            if (handler2 != null) {
                handler2.post(new Runnable() {
                    public final void run() {
                        Promise.this.lambda$error$10$Promise();
                    }
                });
            } else {
                new Thread(new Runnable() {
                    public final void run() {
                        Promise.this.lambda$error$11$Promise();
                    }
                }).start();
            }
        }
    }

    public  void lambda$error$10$Promise() {
        handleError(this.onErrorListener);
    }

    public  void lambda$error$11$Promise() {
        handleError(this.rejectedObject);
    }

    private void handleSuccess(Promise promise, Object obj) {
        OnSuccessListener onSuccessListener2 = this.onSuccessListener;
        if (onSuccessListener2 != null) {
            Object onSuccess = onSuccessListener2.onSuccess(obj);
            if (onSuccess != null) {
                if (onSuccess instanceof Promise) {
                    if (promise != null) {
                        Promise promise2 = (Promise) onSuccess;
                        promise2.onSuccessListener = promise.onSuccessListener;
                        promise2.onErrorListener = promise.onErrorListener;
                        promise2.child = promise.child;
                        if (promise2.isResolved) {
                            promise2.handleSuccess(promise2.child, promise2.resolvedObject);
                        } else if (promise2.isRejected) {
                            promise2.handleError(promise2.rejectedObject);
                        }
                    }
                } else if (promise != null) {
                    promise.resolve(onSuccess);
                }
            } else if (promise != null) {
                promise.resolve(onSuccess);
            }
        }
    }

    private void handleError(Object obj) {
        OnErrorListener onErrorListener2 = this.onErrorListener;
        if (onErrorListener2 != null) {
            onErrorListener2.onError(obj);
            return;
        }
        Promise promise = this.child;
        if (promise != null) {
            promise.reject(obj);
        }
    }


    public Object getTag() {
        return this.tag;
    }


    public void setTag(Object obj) {
        this.tag = obj;
    }
}
