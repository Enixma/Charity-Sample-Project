package com.enixma.sample.charity.domain

import io.reactivex.Observable

interface UseCase<in P : UseCase.Request, Q : UseCase.Result> {

    fun execute(request: P): Observable<Q>

    interface Request

    interface Result
}