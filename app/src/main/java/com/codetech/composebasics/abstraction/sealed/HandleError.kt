package com.codetech.composebasics.abstraction.sealed

import android.accounts.NetworkErrorException
import android.util.Log
import java.io.IOException
import java.lang.reflect.InvocationTargetException

fun <T> handleError(performSafeAction: () -> T): Result<T> {
    val TAG = "handlerErrorInfo"
    return try {
        val result = performSafeAction()
        Result.Success(result)
    } catch (e: NullPointerException) {
        e.printStackTrace()
        Log.d(TAG, "handleError: ${e.message}")
        Result.Failure(e.message.toString())
    } catch (e: NetworkErrorException) {
        e.printStackTrace()
        Log.d(TAG, "handleError: ${e.message}")
        Result.Failure(e.message.toString())
    } catch (e: ClassCastException) {
        e.printStackTrace()
        Log.d(TAG, "handleError: ${e.message}")
        Result.Failure(e.message.toString())
    } catch (e: IllegalStateException) {
        e.printStackTrace()
        Log.d(TAG, "handleError: ${e.message}")
        Result.Failure(e.message.toString())
    } catch (e: ArrayIndexOutOfBoundsException) {
        e.printStackTrace()
        Log.d(TAG, "handleError: ${e.message}")
        Result.Failure(e.message.toString())
    } catch (e: IndexOutOfBoundsException) {
        e.printStackTrace()
        Log.d(TAG, "handleError: ${e.message}")
        Result.Failure(e.message.toString())
    } catch (e: IllegalArgumentException) {
        e.printStackTrace()
        Log.d(TAG, "handleError: ${e.message}")
        Result.Failure(e.message.toString())
    } catch (e: IOException) {
        e.printStackTrace()
        Log.d(TAG, "handleError: ${e.message}")
        Result.Failure(e.message.toString())
    } catch (e: RuntimeException) {
        e.printStackTrace()
        Log.d(TAG, "handleError: ${e.message}")
        Result.Failure(e.message.toString())
    } catch (e: ExceptionInInitializerError) {
        e.printStackTrace()
        Log.d(TAG, "handleError: ${e.message}")
        Result.Failure(e.message.toString())
    } catch (e: TypeCastException) {
        e.printStackTrace()
        Log.d(TAG, "handleError: ${e.message}")
        Result.Failure(e.message.toString())
    } catch (e: SecurityException) {
        e.printStackTrace()
        Log.d(TAG, "handleError: ${e.message}")
        Result.Failure(e.message.toString())
    } catch (e: InvocationTargetException) {
        e.printStackTrace()
        Log.d(TAG, "handleError: ${e.message}")
        Result.Failure(e.message.toString())
    } catch (e: Exception) {
        e.printStackTrace()
        Log.d(TAG, "handleError: ${e.message}")
        Result.Failure(e.message.toString())
    }
}