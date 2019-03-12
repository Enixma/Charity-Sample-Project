package com.enixma.sample.charity.domain

import io.reactivex.Flowable

interface UseCase<in P : UseCase.Request, Q : UseCase.Result> {

    fun execute(request: P): Flowable<Q>

    interface Request

    interface Result
}