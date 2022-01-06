package com.example.mycoffeeshopposter.objects;

import androidx.constraintlayout.helper.widget.Flow;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.VirtualLayout;

public class MenuGroupContainer {
    Flow flowGroup;
    ConstraintLayout clGroup;
    public void makeBox (Flow flow, ConstraintLayout clayout){
        flowGroup = flow;
        clGroup = clayout;
    }

    public Flow getFlowGroup() {
        return flowGroup;
    }

    public ConstraintLayout getClGroup() {
        return clGroup;
    }
}
