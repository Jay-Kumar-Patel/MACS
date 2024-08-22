import React from "react";
import { Button } from "@/components/ui/button";
import Link from "next/link";

export default function TaskHeader(){
    return(
        <div className="flex justify-between items-center">
        <div>
          <h1 className="text-2xl">Tasks</h1>
        </div>
        <div>
          <Link href="/taskform">
            <Button variant="outline">Add Task</Button>
          </Link>
        </div>
      </div>
    )
}