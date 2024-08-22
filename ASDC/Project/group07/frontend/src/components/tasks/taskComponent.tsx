// @ts-nocheck

import { ImCross } from "react-icons/im";
import { LuPencilLine } from "react-icons/lu";
import { FaUser } from "react-icons/fa";
import { TaskType } from "@/types/types";
import { useAuth } from "@/context/AuthContext";
import { useToast } from "@/components/ui/use-toast";
import { useState } from "react";
import { useRouter } from "next/navigation";
import config from "@/config";
import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
  AlertDialogTrigger,
} from "@/components/ui/alert-dialog";

interface TaskComponentProps {
  task: TaskType;
  key: number;
  fetchData: any;
}

export default function TaskComponent({ task, fetchData }: TaskComponentProps) {
  const { token } = useAuth();
  const { toast } = useToast();
  const router = useRouter();

  const [deleteClick, setDeleteClick] = useState(false);
  const [editMode, setEditMode] = useState(false);
  const handleDelete = () => {
    setDeleteClick(true);
  };

  async function removeTask() {
    try {
      const response = await fetch(`${config.apiBaseUrl}/task/delete?id=${task.id}`, {
        method: 'DELETE',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json',
        },
      });

      if (!response.ok) {
        throw new Error("Failed to delete the list");
      }

      toast({
        title: "Success",
        description: "Item deleted successfully",
      });

      setDeleteClick(false);
      fetchData();
    } catch (error) {
      toast({
        title: "Error",
        description: error.message,
      });
    }
  }

  return (
    <div className="px-10 py-8">
        <div className="px-3 py-6 border-2 rounded border-black">
          <div className="flex justify-between items-center">
            <h1 className="text-xl">{task.name}</h1>
            <div className="flex items-center">
              <ImCross className="text-xl mx-2" onClick={handleDelete} />
              <LuPencilLine className="text-2xl mx-2" onClick={() => router.push(`/tasks/${task.id}`)} /> {/* Set edit mode to true on click */}
            </div>
          </div>

          <div className="flex justify-between mt-4 w-[40%]">
            <div>
              <p>Due on {task.taskDate}</p>
            </div>
            <div className="flex items-center">
              <FaUser className="mx-2" />
              <p>{task.assignedTo.firstName} {task.assignedTo.lastName}</p>
            </div>
          </div>
          <div className="mt-3">
            <p>{task.description}</p>
          </div>
        </div>
      

      <AlertDialog open={deleteClick}>
        <AlertDialogContent>
          <AlertDialogHeader>
            <AlertDialogTitle>Are you sure?</AlertDialogTitle>
            <AlertDialogDescription>
              This action cannot be undone. This will permanently delete your task
              and remove your data from our servers.
            </AlertDialogDescription>
          </AlertDialogHeader>
          <AlertDialogFooter>
            <AlertDialogCancel onClick={() => setDeleteClick(false)}>Cancel</AlertDialogCancel>
            <AlertDialogAction onClick={removeTask}>Continue</AlertDialogAction>
          </AlertDialogFooter>
        </AlertDialogContent>
      </AlertDialog>
    </div>
  );
}