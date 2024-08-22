import React from "react";
import ProtectedRoute from "@/components/protected-route";
import Announcements from "@/components/announcements/announcements";
import { PageHeader } from "@/components/page-header";
import AnnouncementComponent from "@/components/announcements/announcementComponent";

export default function AnnouncementsPage() {
  return (
    <ProtectedRoute>
      <PageHeader />
      <div className="w-full py-8 px-4 md:px-6">
        <Announcements />
        {/* <AnnouncementComponent/> */}
      </div>
    </ProtectedRoute>
  );
}
