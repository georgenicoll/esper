/*
 * *************************************************************************************
 *  Copyright (C) 2006-2015 EsperTech, Inc. All rights reserved.                       *
 *  http://www.espertech.com/esper                                                     *
 *  http://www.espertech.com                                                           *
 *  ---------------------------------------------------------------------------------- *
 *  The software in this package is published under the terms of the GPL license       *
 *  a copy of which has been included with this distribution in the license.txt file.  *
 * *************************************************************************************
 */

package com.espertech.esper.support.bean.bookexample;

public class Review
{
    private int reviewId;
    private String comment;

    public Review(int reviewId, String comment)
    {
        this.reviewId = reviewId;
        this.comment = comment;
    }

    public int getReviewId()
    {
        return reviewId;
    }

    public String getComment()
    {
        return comment;
    }
}
