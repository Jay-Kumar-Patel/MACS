import React from "react";
import { Card } from "@/components/ui/card";
import { UserType } from "@/types/types";
import { Avatar, AvatarFallback } from "../ui/avatar";
import { cn, getFullNameFromUser, getInitialsFromUser } from "@/lib/utils";
import UserAvatar from "../user-avatar";

interface UserCardProps {
  user: UserType;
  className?: string;
}

const UserCard: React.FC<UserCardProps> = ({ user, className = "" }) => {
  return (
    <Card className={cn("w-full max-w-sm p-6 grid gap-6", className)}>
      <div className="flex items-center gap-4">
        <UserAvatar user={user} />
        <div className="grid gap-1">
          <div className="text-xl font-semibold">
            {getFullNameFromUser(user)}
          </div>
          <div className="text-muted-foreground">{user.email}</div>
        </div>
      </div>
    </Card>
  );
};

export default UserCard;
